package com.neolearn.projects_service.project.application.internal.queryservices;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.domain.model.entities.*;
import com.neolearn.projects_service.project.domain.model.queries.*;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectQueryServiceImpl implements ProjectQueryService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final InvitationRepository invitationRepository;
    private final ResourceRepository resourceRepository;
    private final MessageRepository messageRepository;
    private final TaskRepository taskRepository;
    private final RequirementRepository requirementRepository;
    private final TechnologyRepository technologyRepository;
    private final ProjectTechnologyRepository projectTechnologyRepository;
    private final UserRepository userRepository;

    public ProjectQueryServiceImpl(
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            InvitationRepository invitationRepository,
            ResourceRepository resourceRepository,
            MessageRepository messageRepository,
            TaskRepository taskRepository,
            RequirementRepository requirementRepository,
            TechnologyRepository technologyRepository,
            ProjectTechnologyRepository projectTechnologyRepository,
            UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.invitationRepository = invitationRepository;
        this.resourceRepository = resourceRepository;
        this.messageRepository = messageRepository;
        this.taskRepository = taskRepository;
        this.requirementRepository = requirementRepository;
        this.technologyRepository = technologyRepository;
        this.projectTechnologyRepository = projectTechnologyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Project> handle(GetProjectByIdQuery query) {
        return projectRepository.findById(query.id());
    }

    @Override
    public List<Project> handle(GetPlatformProjectsQuery query) {
        return projectRepository.findByEsPredefinido(true);
    }

    @Override
    public List<Project> handle(GetUserProjectsQuery query) {
        // Primero obtener el usuario por username
        Optional<User> userOpt = userRepository.findByNombreUsuario(query.username());
        if (userOpt.isEmpty()) {
            return Collections.emptyList();
        }
        
        Long userId = userOpt.get().getId();
        
        // Obtener los proyectos donde el usuario es miembro
        List<ProjectMember> memberships = projectMemberRepository.findByIdIdUsuario(userId);
        
        if (memberships.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Obtener los IDs de los proyectos
        List<Long> projectIds = memberships.stream()
                .map(member -> member.getId().getIdProyecto())
                .toList();
        
        return projectRepository.findAllById(projectIds);
    }

    @Override
    public List<ProjectMember> handle(GetProjectMembersQuery query) {
        return projectMemberRepository.findByIdIdProyecto(query.idProyecto());
    }

    @Override
    public List<Invitation> handle(GetProjectInvitationsQuery query) {
        return invitationRepository.findByIdProyecto(query.idProyecto());
    }

    @Override
    public List<Invitation> handle(GetUserInvitationsQuery query) {
        return invitationRepository.findByIdUsuarioInvitado(query.idUsuario());
    }

    @Override
    public List<Resource> handle(GetProjectResourcesQuery query) {
        return resourceRepository.findByIdProyecto(query.idProyecto());
    }

    @Override
    public List<Message> handle(GetProjectMessagesQuery query) {
        return messageRepository.findByIdProyectoOrderByFechaEnvioDesc(query.idProyecto());
    }

    @Override
    public List<Task> handle(GetProjectTasksQuery query) {
        return taskRepository.findByIdProyecto(query.idProyecto());
    }

    @Override
    public List<Task> handle(GetUserTasksQuery query) {
        if (query.idProyecto() != null) {
            return taskRepository.findByIdProyectoAndIdUsuarioAsignado(
                    query.idProyecto(), query.idUsuario());
        } else {
            return taskRepository.findByIdUsuarioAsignado(query.idUsuario());
        }
    }

    @Override
    public List<Requirement> handle(GetProjectRequirementsQuery query) {
        if (query.tipo() != null) {
            return requirementRepository.findByIdProyectoAndTipo(
                    query.idProyecto(), query.tipo());
        } else {
            return requirementRepository.findByIdProyecto(query.idProyecto());
        }
    }

    @Override
    public List<Technology> handle(GetProjectTechnologiesQuery query) {
        // Obtener las relaciones de proyecto-tecnología
        List<ProjectTechnology> projectTechnologies = projectTechnologyRepository.findByIdIdProyecto(query.idProyecto());
        
        if (projectTechnologies.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Obtener los IDs de las tecnologías
        List<Long> technologyIds = projectTechnologies.stream()
                .map(pt -> pt.getId().getIdTecnologia())
                .toList();
        
        return technologyRepository.findAllById(technologyIds);
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.id());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByNombreUsuario(query.nombreUsuario());
    }


} 