package com.neolearn.projects_service.application.service;

import com.neolearn.projects_service.domain.model.*;
import com.neolearn.projects_service.domain.repository.*;
import com.neolearn.projects_service.domain.exception.*;
import com.neolearn.projects_service.application.dto.project.*;
import com.neolearn.projects_service.application.dto.invitation.*;
import com.neolearn.projects_service.application.dto.member.*;
import com.neolearn.projects_service.application.dto.resource.*;
import com.neolearn.projects_service.application.dto.task.*;
import com.neolearn.projects_service.application.dto.chat.ProjectChatInfoResponse;
import com.neolearn.projects_service.infrastructure.chat.SendbirdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectApplicationService.class);

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final InvitationRepository invitationRepository;
    private final ResourceRepository resourceRepository;
    private final TaskRepository taskRepository;
    private final SendbirdService sendbirdService;

    public ProjectApplicationService(ProjectRepository projectRepository, ProjectMemberRepository memberRepository,
                                     InvitationRepository invitationRepository, ResourceRepository resourceRepository,
                                     TaskRepository taskRepository, SendbirdService sendbirdService) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.invitationRepository = invitationRepository;
        this.resourceRepository = resourceRepository;
        this.taskRepository = taskRepository;
        this.sendbirdService = sendbirdService;
    }

    // --- US043, US044, US047: Creación y Publicación de Proyectos ---
    public ProjectResponse createProject(CreateProjectRequest dto, Long creatorUserId) {
        if (!StringUtils.hasText(dto.getName())) throw new ProjectOperationException("Project name is required.");
        if (dto.getProjectType() == null) throw new ProjectOperationException("Project type is required.");
        if (creatorUserId == null) throw new ProjectOperationException("Creator User ID is required.");

        logger.info("Service: Creating project '{}' by user {}", dto.getName(), creatorUserId);
        sendbirdService.ensureUserExistsInSendbird(String.valueOf(creatorUserId), "User " + creatorUserId, null);

        Project project = Project.builder()
                .name(dto.getName()).description(dto.getDescription()).projectType(dto.getProjectType())
                .ownerUserId(creatorUserId).license(dto.getProjectType() == ProjectType.OPEN_SOURCE ? dto.getLicense() : null)
                .build();
        Project savedProject = projectRepository.save(project);
        logger.info("Service: Project {} saved with ID {}", savedProject.getName(), savedProject.getId());

        String sendbirdChannelId = sendbirdService.createGroupChannelForProject(
                savedProject.getName(), savedProject.getId(), List.of(String.valueOf(creatorUserId)), null);
        savedProject.setSendbirdChannelId(sendbirdChannelId);
        projectRepository.save(savedProject);
        logger.info("Service: Sendbird channel {} associated with project ID {}", sendbirdChannelId, savedProject.getId());

        ProjectMember ownerAsMember = ProjectMember.builder()
                .project(savedProject).userId(creatorUserId).role(MemberRole.ADMIN).build();
        memberRepository.save(ownerAsMember);
        logger.info("Service: User {} added as ADMIN to project ID {}", creatorUserId, savedProject.getId());

        return mapToProjectResponse(savedProject);
    }

    @Transactional(readOnly = true)
    public Optional<ProjectResponse> getProjectById(Long projectId, Long currentUserId) {
        logger.debug("Service: Fetching project by ID: {} for user {}", projectId, currentUserId);
        return projectRepository.findById(projectId).map(this::mapToProjectResponse);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects(ProjectType typeFilter, Long memberUserIdFilter) {
        logger.debug("Service: Fetching all projects with typeFilter: {} and memberUserIdFilter: {}", typeFilter, memberUserIdFilter);
        if (memberUserIdFilter != null) {
            return memberRepository.findByUserId(memberUserIdFilter).stream()
                    .map(ProjectMember::getProject).filter(p -> typeFilter == null || p.getProjectType() == typeFilter)
                    .distinct().map(this::mapToProjectResponse).collect(Collectors.toList());
        } else if (typeFilter != null) {
            return projectRepository.findByProjectType(typeFilter).stream()
                    .map(this::mapToProjectResponse).collect(Collectors.toList());
        }
        return projectRepository.findAll().stream().map(this::mapToProjectResponse).collect(Collectors.toList());
    }

    public ProjectResponse updateProjectDetails(Long projectId, UpdateProjectRequest dto, Long currentUserId) {
        logger.info("Service: User {} updating project ID {}", currentUserId, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));
        if (!project.isUserAdmin(currentUserId)) {
            throw new UnauthorizedOperationException("User " + currentUserId + " is not authorized to update project " + projectId);
        }
        if (StringUtils.hasText(dto.getName())) project.setName(dto.getName());
        if (dto.getDescription() != null) project.setDescription(dto.getDescription());
        if (project.getProjectType() == ProjectType.OPEN_SOURCE && dto.getLicense() != null) {
            project.setLicense(dto.getLicense());
        }
        Project updatedProject = projectRepository.save(project);
        logger.info("Service: Project ID {} updated.", projectId);
        return mapToProjectResponse(updatedProject);
    }

    // --- US049: Invitar Miembros ---
    public InvitationResponse inviteMemberToProject(Long projectId, InviteMemberRequest dto, Long inviterUserId) {
        if (dto.getInvitedUserId() == null) throw new ProjectOperationException("Invited User ID is required.");
        logger.info("Service: User {} inviting user {} to project ID {}", inviterUserId, dto.getInvitedUserId(), projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserAdmin(inviterUserId)) {
            throw new UnauthorizedOperationException("User " + inviterUserId + " is not admin of project " + projectId);
        }
        if (dto.getInvitedUserId().equals(inviterUserId)) throw new ProjectOperationException("Cannot invite yourself.");
        if (memberRepository.findByProjectIdAndUserId(projectId, dto.getInvitedUserId()).isPresent()) {
            throw new ProjectOperationException("User " + dto.getInvitedUserId() + " is already a member.");
        }
        Optional<Invitation> existingInv = invitationRepository.findByProjectIdAndInvitedUserId(projectId, dto.getInvitedUserId())
                .filter(inv -> inv.getStatus() == InvitationStatus.PENDING);
        if (existingInv.isPresent()) {
            logger.warn("Service: User {} already has a pending invitation for project {}", dto.getInvitedUserId(), projectId);
            return mapToInvitationResponse(existingInv.get());
        }
        Invitation invitation = Invitation.builder()
                .project(project).invitedUserId(dto.getInvitedUserId()).inviterUserId(inviterUserId).build();
        Invitation savedInvitation = invitationRepository.save(invitation);
        logger.info("Service: Invitation ID {} created for user {} to project ID {}", savedInvitation.getId(), dto.getInvitedUserId(), projectId);
        return mapToInvitationResponse(savedInvitation);
    }

    // --- US050: Aceptar/Rechazar Invitación ---
    @Transactional(readOnly = true)
    public List<InvitationResponse> getPendingInvitationsForUser(Long userId) {
        logger.debug("Service: Fetching pending invitations for user {}", userId);
        return invitationRepository.findByInvitedUserIdAndStatus(userId, InvitationStatus.PENDING)
                .stream().map(this::mapToInvitationResponse).collect(Collectors.toList());
    }

    public ProjectMemberResponse respondToInvitation(Long invitationId, HandleInvitationRequest dto, Long respondingUserId) {
        if (dto.getAccepted() == null) throw new ProjectOperationException("Acceptance status is required.");
        logger.info("Service: User {} responding to invitation ID {} with accepted: {}", respondingUserId, invitationId, dto.getAccepted());
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found: " + invitationId));
        if (!invitation.getInvitedUserId().equals(respondingUserId)) {
            throw new UnauthorizedOperationException("User " + respondingUserId + " not authorized for invitation " + invitationId);
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ProjectOperationException("Invitation " + invitationId + " already processed.");
        }
        invitation.setRespondedAt(LocalDateTime.now());
        if (dto.getAccepted()) {
            invitation.setStatus(InvitationStatus.ACCEPTED);
            Project project = invitation.getProject();
            if (memberRepository.findByProjectIdAndUserId(project.getId(), respondingUserId).isPresent()) {
                invitationRepository.save(invitation);
                logger.warn("Service: User {} accepted invitation but was already member of project {}", respondingUserId, project.getId());
                return mapToProjectMemberResponse(memberRepository.findByProjectIdAndUserId(project.getId(), respondingUserId).get());
            }
            ProjectMember newMember = ProjectMember.builder()
                    .project(project).userId(respondingUserId).role(MemberRole.MEMBER).build();
            ProjectMember savedMember = memberRepository.save(newMember);
            if (project.getSendbirdChannelId() != null) {
                sendbirdService.addUserToChannel(project.getSendbirdChannelId(), String.valueOf(respondingUserId));
            }
            invitationRepository.save(invitation);
            logger.info("Service: User {} accepted invitation ID {} and joined project ID {}", respondingUserId, invitationId, project.getId());
            return mapToProjectMemberResponse(savedMember);
        } else {
            invitation.setStatus(InvitationStatus.REJECTED);
            invitationRepository.save(invitation);
            logger.info("Service: User {} rejected invitation ID {}", respondingUserId, invitationId);
            throw new ProjectOperationException("Invitation rejected by user.");
        }
    }

    // --- US054: Administrar Grupo ---
    public void removeMemberFromProject(Long projectId, Long userIdToRemove, Long currentAdminUserId) {
        logger.info("Service: Admin {} attempting to remove user {} from project ID {}", currentAdminUserId, userIdToRemove, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserAdmin(currentAdminUserId)) {
            throw new UnauthorizedOperationException("User " + currentAdminUserId + " is not admin of project " + projectId);
        }
        if (project.getOwnerUserId().equals(userIdToRemove)) {
            throw new ProjectOperationException("Cannot remove the project owner (ID: " + userIdToRemove + ").");
        }
        ProjectMember memberToRemove = memberRepository.findByProjectIdAndUserId(projectId, userIdToRemove)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userIdToRemove + " not found as member in project " + projectId));
        memberRepository.delete(memberToRemove);
        logger.info("Service: User {} removed from project ID {}", userIdToRemove, projectId);
        if (project.getSendbirdChannelId() != null) {
            sendbirdService.removeUserFromChannel(project.getSendbirdChannelId(), String.valueOf(userIdToRemove));
        }
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getProjectMembers(Long projectId) {
        logger.debug("Service: Fetching members for project ID {}", projectId);
        if (!projectRepository.existsById(projectId)) throw new ResourceNotFoundException("Project not found: " + projectId);
        return memberRepository.findByProjectId(projectId).stream()
                .map(this::mapToProjectMemberResponse).collect(Collectors.toList());
    }

    // --- US051: Compartir Recursos ---
    public ResourceResponse addResourceToProject(Long projectId, CreateResourceRequest dto, Long uploaderUserId) {
        if (!StringUtils.hasText(dto.getName())) throw new ProjectOperationException("Resource name is required.");
        if (!StringUtils.hasText(dto.getType())) throw new ProjectOperationException("Resource type is required.");
        if (!StringUtils.hasText(dto.getLocation())) throw new ProjectOperationException("Resource location is required.");

        logger.info("Service: User {} adding resource '{}' to project ID {}", uploaderUserId, dto.getName(), projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserMember(uploaderUserId)) {
            throw new UnauthorizedOperationException("User " + uploaderUserId + " is not a member of project " + projectId);
        }
        // Lógica de subida de archivos no está aquí, se asume que 'location' es una URL o referencia.
        Resource resource = Resource.builder()
                .project(project).name(dto.getName()).type(dto.getType().toUpperCase())
                .location(dto.getLocation()).uploadedByUserId(uploaderUserId).build();
        Resource savedResource = resourceRepository.save(resource);
        logger.info("Service: Resource ID {} added to project ID {}", savedResource.getId(), projectId);
        return mapToResourceResponse(savedResource);
    }

    @Transactional(readOnly = true)
    public List<ResourceResponse> getProjectResources(Long projectId, Long currentUserId) {
        logger.debug("Service: Fetching resources for project ID {} by user {}", projectId, currentUserId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserMember(currentUserId)) {
            throw new UnauthorizedOperationException("User " + currentUserId + " is not a member of project " + projectId);
        }
        return resourceRepository.findByProjectId(projectId).stream()
                .map(this::mapToResourceResponse).collect(Collectors.toList());
    }

    public void deleteResource(Long projectId, Long resourceId, Long currentUserId) {
        logger.info("Service: User {} attempting to delete resource ID {} from project ID {}", currentUserId, resourceId, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + resourceId));
        if (!resource.getProject().getId().equals(projectId)) {
            throw new ProjectOperationException("Resource " + resourceId + " does not belong to project " + projectId);
        }
        if (!(resource.getUploadedByUserId().equals(currentUserId) || project.isUserAdmin(currentUserId))) {
            throw new UnauthorizedOperationException("User " + currentUserId + " not authorized to delete resource " + resourceId);
        }
        resourceRepository.delete(resource);
        logger.info("Service: Resource ID {} deleted by user {}", resourceId, currentUserId);
    }

    // --- US052: Comunicación (Sendbird) ---
    @Transactional(readOnly = true)
    public ProjectChatInfoResponse getProjectChatInformation(Long projectId, Long userIdRequestingAccess) {
        logger.debug("Service: User {} requesting chat info for project ID {}", userIdRequestingAccess, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserMember(userIdRequestingAccess)) {
            throw new UnauthorizedOperationException("User " + userIdRequestingAccess + " is not member of project " + projectId);
        }
        if (!StringUtils.hasText(project.getSendbirdChannelId())) {
            throw new ProjectOperationException("Chat not configured for project " + projectId);
        }
        sendbirdService.ensureUserExistsInSendbird(String.valueOf(userIdRequestingAccess), "User " + userIdRequestingAccess, null);
        String sendbirdUserAccessToken = sendbirdService.generateUserSessionToken(String.valueOf(userIdRequestingAccess));
        return new ProjectChatInfoResponse(String.valueOf(projectId), project.getSendbirdChannelId(),
                "neolearn_user_" + userIdRequestingAccess, sendbirdUserAccessToken);
    }

    // --- US053: Tareas ---
    public TaskResponse createTaskInProject(Long projectId, CreateTaskRequest dto, Long creatorUserId) {
        if (!StringUtils.hasText(dto.getTitle())) throw new ProjectOperationException("Task title is required.");
        logger.info("Service: User {} creating task '{}' in project ID {}", creatorUserId, dto.getTitle(), projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserMember(creatorUserId)) {
            throw new UnauthorizedOperationException("User " + creatorUserId + " is not a member of project " + projectId);
        }
        if (dto.getAssignedToUserId() != null && !project.isUserMember(dto.getAssignedToUserId())) {
            throw new ProjectOperationException("User " + dto.getAssignedToUserId() + " (assignee) is not a member of project " + projectId);
        }
        Task task = Task.builder()
                .project(project).title(dto.getTitle()).description(dto.getDescription())
                .assignedToUserId(dto.getAssignedToUserId()).createdByUserId(creatorUserId)
                .dueDate(dto.getDueDate()).build();
        Task savedTask = taskRepository.save(task);
        logger.info("Service: Task ID {} created in project ID {} by user {}", savedTask.getId(), projectId, creatorUserId);
        return mapToTaskResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public Optional<TaskResponse> getTaskById(Long projectId, Long taskId, Long currentUserId) {
        logger.debug("Service: User {} fetching task ID {} for project ID {}", currentUserId, taskId, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserMember(currentUserId)) {
            throw new UnauthorizedOperationException("User " + currentUserId + " is not a member of project " + projectId);
        }
        return taskRepository.findById(taskId)
                .filter(task -> task.getProject().getId().equals(projectId)).map(this::mapToTaskResponse);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksForProject(Long projectId, Long currentUserId, TaskStatus statusFilter) {
        logger.debug("Service: User {} fetching tasks for project ID {} with status filter {}", currentUserId, projectId, statusFilter);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserMember(currentUserId)) {
            throw new UnauthorizedOperationException("User " + currentUserId + " is not a member of project " + projectId);
        }
        List<Task> tasks = (statusFilter != null) ?
                taskRepository.findByProjectIdAndStatus(projectId, statusFilter) :
                taskRepository.findByProjectId(projectId);
        return tasks.stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long projectId, Long taskId, UpdateTaskRequest dto, Long currentUserId) {
        logger.info("Service: User {} updating task ID {} in project ID {}", currentUserId, taskId, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new ProjectOperationException("Task " + taskId + " does not belong to project " + projectId);
        }
        if (!(project.isUserAdmin(currentUserId) || task.getCreatedByUserId().equals(currentUserId))) {
            throw new UnauthorizedOperationException("User " + currentUserId + " not authorized to update task " + taskId);
        }
        if (dto.getAssignedToUserId() != null && !project.isUserMember(dto.getAssignedToUserId())) {
            throw new ProjectOperationException("User " + dto.getAssignedToUserId() + " (assignee) is not a member of project " + projectId);
        }
        if (StringUtils.hasText(dto.getTitle())) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        task.setAssignedToUserId(dto.getAssignedToUserId()); // Permite nulo para desasignar
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        if (dto.getDueDate() != null) task.setDueDate(dto.getDueDate());
        Task updatedTask = taskRepository.save(task);
        logger.info("Service: Task ID {} updated by user {}", taskId, currentUserId);
        return mapToTaskResponse(updatedTask);
    }

    public TaskResponse assignTaskToMember(Long projectId, Long taskId, AssignTaskRequest dto, Long assignerUserId) {
        logger.info("Service: User {} assigning task ID {} to user {} in project ID {}", assignerUserId, taskId, dto.getMemberUserIdToAssign(), projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        if (!project.isUserAdmin(assignerUserId)) {
            throw new UnauthorizedOperationException("User " + assignerUserId + " not authorized to assign tasks in project " + projectId);
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new ProjectOperationException("Task " + taskId + " does not belong to project " + projectId);
        }
        if (dto.getMemberUserIdToAssign() != null) {
            if (!memberRepository.findByProjectIdAndUserId(projectId, dto.getMemberUserIdToAssign()).isPresent()) {
                throw new ResourceNotFoundException("User " + dto.getMemberUserIdToAssign() + " is not a member of project " + projectId);
            }
            task.setAssignedToUserId(dto.getMemberUserIdToAssign());
        } else {
            task.setAssignedToUserId(null); // Desasignar
        }
        Task updatedTask = taskRepository.save(task);
        logger.info("Service: Task ID {} assignment updated by admin {}", taskId, assignerUserId);
        return mapToTaskResponse(updatedTask);
    }

    public void deleteTask(Long projectId, Long taskId, Long currentUserId) {
        logger.info("Service: User {} deleting task ID {} from project ID {}", currentUserId, taskId, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new ProjectOperationException("Task " + taskId + " does not belong to project " + projectId);
        }
        if (!(project.isUserAdmin(currentUserId) || task.getCreatedByUserId().equals(currentUserId))) {
            throw new UnauthorizedOperationException("User " + currentUserId + " not authorized to delete task " + taskId);
        }
        taskRepository.delete(task);
        logger.info("Service: Task ID {} deleted by user {}", taskId, currentUserId);
    }

    // --- Métodos de Mapeo Privados ---
    private ProjectResponse mapToProjectResponse(Project p) {
        return new ProjectResponse(p.getId(),p.getName(),p.getDescription(),p.getProjectType(),p.getOwnerUserId(),p.getLicense(),p.getSendbirdChannelId(),p.getCreatedAt(),p.getUpdatedAt());
    }
    private InvitationResponse mapToInvitationResponse(Invitation i) {
        return new InvitationResponse(i.getId(),i.getProject().getId(),i.getInvitedUserId(),i.getInviterUserId(),i.getStatus(),i.getCreatedAt(),i.getRespondedAt());
    }
    private ProjectMemberResponse mapToProjectMemberResponse(ProjectMember m) {
        return new ProjectMemberResponse(m.getId(),m.getProject().getId(),m.getUserId(),m.getRole(),m.getJoinedAt());
    }
    private ResourceResponse mapToResourceResponse(Resource r) {
        return new ResourceResponse(r.getId(),r.getProject().getId(),r.getName(),r.getType(),r.getLocation(),r.getUploadedByUserId(),r.getCreatedAt());
    }
    private TaskResponse mapToTaskResponse(Task t) {
        return new TaskResponse(t.getId(),t.getProject().getId(),t.getTitle(),t.getDescription(),t.getAssignedToUserId(),t.getCreatedByUserId(),t.getStatus(),t.getDueDate(),t.getCreatedAt(),t.getUpdatedAt());
    }
}