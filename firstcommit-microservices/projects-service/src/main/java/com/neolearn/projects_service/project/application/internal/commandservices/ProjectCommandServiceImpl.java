package com.neolearn.projects_service.project.application.internal.commandservices;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.domain.model.commands.*;
import com.neolearn.projects_service.project.domain.model.entities.*;
import com.neolearn.projects_service.project.domain.model.valueobjects.InvitationStatus;
import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectRole;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.*;
import com.neolearn.projects_service.shared.application.services.UserSyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjectCommandServiceImpl implements ProjectCommandService {

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
    private final UserSyncService userSyncService;

    public ProjectCommandServiceImpl(
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            InvitationRepository invitationRepository,
            ResourceRepository resourceRepository,
            MessageRepository messageRepository,
            TaskRepository taskRepository,
            RequirementRepository requirementRepository,
            TechnologyRepository technologyRepository,
            ProjectTechnologyRepository projectTechnologyRepository,
            UserRepository userRepository,
            UserSyncService userSyncService) {
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
        this.userSyncService = userSyncService;
    }

    @Override
    @Transactional
    public Optional<Project> handle(CreateProjectCommand command) {
        // Verificar si ya existe un proyecto con el mismo nombre
        if (projectRepository.existsByNombre(command.nombre())) {
            throw new IllegalStateException("Ya existe un proyecto con este nombre");
        }

        // Asegurar que el usuario creador existe (auto-crear si es necesario)
        User createdUser = userSyncService.ensureUserExists(command.usernameCreador());

        // Crear el proyecto
        Project project = new Project(
                command.nombre(),
                command.descripcionGeneral(),
                command.urlRepositorio(),
                command.usernameCreador(),
                command.esPredefinido()
        );

        Project savedProject = projectRepository.save(project);

        // Añadir al creador como miembro administrador usando el usuario ya creado/encontrado
        ProjectMember member = new ProjectMember(
                savedProject.getId(),
                createdUser.getId(),
                ProjectRole.ADMINISTRADOR
        );

        projectMemberRepository.save(member);

        return Optional.of(savedProject);
    }

    @Override
    @Transactional
    public Optional<Project> handle(UpdateProjectCommand command) {
        return projectRepository.findById(command.idProyecto())
                .map(project -> {
                    // Actualizar nombre si se proporciona
                    if (command.nombre() != null && !command.nombre().isBlank()) {
                        // Verificar si ya existe otro proyecto con el mismo nombre
                        if (!project.getNombre().equals(command.nombre()) && 
                            projectRepository.existsByNombre(command.nombre())) {
                            throw new IllegalStateException("Ya existe un proyecto con este nombre");
                        }
                        project.updateNombre(command.nombre());
                    }
                    
                    // Actualizar descripción general si se proporciona
                    if (command.descripcionGeneral() != null) {
                        project.updateDescripcionGeneral(command.descripcionGeneral());
                    }
                    
                    // Actualizar URL del repositorio si se proporciona
                    if (command.urlRepositorio() != null) {
                        project.updateUrlRepositorio(command.urlRepositorio());
                    }
                    
                    return projectRepository.save(project);
                });
    }

    @Override
    @Transactional
    public Optional<Project> handle(UpdateProjectStatusCommand command) {
        return projectRepository.findById(command.idProyecto())
                .map(project -> {
                    project.updateEstado(command.nuevoEstado());
                    return projectRepository.save(project);
                });
    }

    @Override
    @Transactional
    public Optional<Invitation> handle(InviteMemberCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Verificar si el usuario invitador es miembro del proyecto
        if (!projectMemberRepository.existsByIdIdProyectoAndIdIdUsuario(command.idProyecto(), command.idUsuarioInvitador())) {
            throw new IllegalStateException("El usuario invitador no es miembro del proyecto");
        }

        // Verificar si el usuario invitado ya es miembro del proyecto
        if (projectMemberRepository.existsByIdIdProyectoAndIdIdUsuario(command.idProyecto(), command.idUsuarioInvitado())) {
            throw new IllegalStateException("El usuario invitado ya es miembro del proyecto");
        }

        // Verificar si ya existe una invitación pendiente para este usuario en este proyecto
        if (invitationRepository.existsByIdProyectoAndIdUsuarioInvitadoAndEstado(
                command.idProyecto(), command.idUsuarioInvitado(), InvitationStatus.PENDIENTE)) {
            throw new IllegalStateException("Ya existe una invitación pendiente para este usuario en este proyecto");
        }

        // Crear la invitación
        Invitation invitation = new Invitation(
                command.idProyecto(),
                command.idUsuarioInvitado(),
                command.idUsuarioInvitador()
        );

        return Optional.of(invitationRepository.save(invitation));
    }

    @Override
    @Transactional
    public Optional<Invitation> handle(RespondToInvitationCommand command) {
        return invitationRepository.findById(command.idInvitacion())
                .map(invitation -> {
                    if (!invitation.isPendiente()) {
                        throw new IllegalStateException("La invitación ya ha sido respondida");
                    }

                    if (command.respuesta() == InvitationStatus.ACEPTADA) {
                        invitation.aceptar();
                        
                        // Añadir al usuario como miembro
                        ProjectMember member = new ProjectMember(
                                invitation.getIdProyecto(),
                                invitation.getIdUsuarioInvitado(),
                                ProjectRole.MIEMBRO
                        );
                        
                        projectMemberRepository.save(member);
                    } else if (command.respuesta() == InvitationStatus.RECHAZADA) {
                        invitation.rechazar();
                    }

                    return invitationRepository.save(invitation);
                });
    }

    @Override
    @Transactional
    public Optional<Resource> handle(ShareResourceCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Verificar si el usuario es miembro del proyecto
        if (!projectMemberRepository.existsByIdIdProyectoAndIdIdUsuario(command.idProyecto(), command.idUsuarioQueComparte())) {
            throw new IllegalStateException("El usuario no es miembro del proyecto");
        }

        // Crear el recurso
        Resource resource = new Resource(
                command.idProyecto(),
                command.idUsuarioQueComparte(),
                command.tipoRecurso(),
                command.valorRecurso(),
                command.nombreVisible()
        );

        return Optional.of(resourceRepository.save(resource));
    }

    @Override
    @Transactional
    public Optional<Message> handle(SendMessageCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Verificar si el usuario es miembro del proyecto
        if (!projectMemberRepository.existsByIdIdProyectoAndIdIdUsuario(command.idProyecto(), command.idUsuarioEmisor())) {
            throw new IllegalStateException("El usuario no es miembro del proyecto");
        }

        // Crear el mensaje
        Message message = new Message(
                command.idProyecto(),
                command.idUsuarioEmisor(),
                command.contenido()
        );

        return Optional.of(messageRepository.save(message));
    }

    @Override
    @Transactional
    public Optional<Task> handle(AssignTaskCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Si hay un usuario asignado, verificar que es miembro del proyecto
        if (command.idUsuarioAsignado() != null) {
            if (!projectMemberRepository.existsByIdIdProyectoAndIdIdUsuario(command.idProyecto(), command.idUsuarioAsignado())) {
                throw new IllegalStateException("El usuario asignado no es miembro del proyecto");
            }
        }

        // Crear la tarea
        Task task = new Task(
                command.idProyecto(),
                command.nombre(),
                command.descripcion(),
                command.idUsuarioAsignado(),
                command.fechaVencimiento()
        );

        return Optional.of(taskRepository.save(task));
    }

    @Override
    @Transactional
    public Optional<Task> handle(UpdateTaskStatusCommand command) {
        return taskRepository.findById(command.idTarea())
                .map(task -> {
                    task.updateEstado(command.nuevoEstado());
                    return taskRepository.save(task);
                });
    }

    @Override
    @Transactional
    public boolean handle(RemoveMemberCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Verificar si el usuario es miembro del proyecto
        if (!projectMemberRepository.existsByIdIdProyectoAndIdIdUsuario(command.idProyecto(), command.idUsuario())) {
            throw new IllegalStateException("El usuario no es miembro del proyecto");
        }

        // No permitir eliminar al último administrador
        Optional<ProjectMember> memberToRemove = projectMemberRepository.findByIdIdProyectoAndIdIdUsuario(
                command.idProyecto(), command.idUsuario());
        
        if (memberToRemove.isPresent() && memberToRemove.get().isAdmin()) {
            long adminCount = projectMemberRepository.findByIdIdProyectoAndRol(
                    command.idProyecto(), ProjectRole.ADMINISTRADOR).size();
            
            if (adminCount <= 1) {
                throw new IllegalStateException("No se puede eliminar al último administrador del proyecto");
            }
        }

        // Eliminar al miembro
        projectMemberRepository.deleteByIdIdProyectoAndIdIdUsuario(command.idProyecto(), command.idUsuario());
        
        return true;
    }

    @Override
    @Transactional
    public Optional<Requirement> handle(AddRequirementCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Crear el requerimiento
        Requirement requirement = new Requirement(
                command.idProyecto(),
                command.descripcion(),
                command.tipo()
        );

        return Optional.of(requirementRepository.save(requirement));
    }

    @Override
    @Transactional
    public Optional<ProjectTechnology> handle(AddTechnologyCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Verificar si la tecnología existe
        if (!technologyRepository.existsById(command.idTecnologia())) {
            throw new IllegalStateException("La tecnología no existe");
        }

        // Verificar si la relación ya existe
        if (projectTechnologyRepository.existsByIdIdProyectoAndIdIdTecnologia(
                command.idProyecto(), command.idTecnologia())) {
            throw new IllegalStateException("Esta tecnología ya está asociada al proyecto");
        }

        // Crear la relación
        ProjectTechnology projectTechnology = new ProjectTechnology(
                command.idProyecto(),
                command.idTecnologia()
        );

        return Optional.of(projectTechnologyRepository.save(projectTechnology));
    }

    @Override
    @Transactional
    public Optional<User> handle(CreateUserCommand command) {
        // Verificar si ya existe un usuario con el mismo nombre de usuario o email
        if (userRepository.existsByNombreUsuario(command.nombreUsuario())) {
            throw new IllegalStateException("Ya existe un usuario con este nombre de usuario");
        }

        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalStateException("Ya existe un usuario con este email");
        }

        // Crear el usuario
        User user = new User(command.nombreUsuario(), command.email());
        
        return Optional.of(userRepository.save(user));
    }

    @Override
    @Transactional
    public Optional<Task> handle(UpdateTaskCommand command) {
        return taskRepository.findById(command.idTarea())
                .map(task -> {
                    // Actualizar nombre si se proporciona
                    if (command.nombre() != null && !command.nombre().isBlank()) {
                        task.updateNombre(command.nombre());
                    }
                    
                    // Actualizar descripción si se proporciona
                    if (command.descripcion() != null) {
                        task.updateDescripcion(command.descripcion());
                    }
                    
                    // Actualizar usuario asignado si se proporciona
                    if (command.idUsuarioAsignado() != null) {
                        // Verificar que el usuario es miembro del proyecto
                        if (!projectMemberRepository.existsByIdIdProyectoAndIdIdUsuario(
                                task.getIdProyecto(), command.idUsuarioAsignado())) {
                            throw new IllegalStateException("El usuario asignado no es miembro del proyecto");
                        }
                        task.asignarUsuario(command.idUsuarioAsignado());
                    }
                    
                    // Actualizar fecha de vencimiento si se proporciona
                    if (command.fechaVencimiento() != null) {
                        task.updateFechaVencimiento(command.fechaVencimiento());
                    }
                    
                    return taskRepository.save(task);
                });
    }

    @Override
    @Transactional
    public boolean handle(RemoveTechnologyCommand command) {
        // Verificar si el proyecto existe
        if (!projectRepository.existsById(command.idProyecto())) {
            throw new IllegalStateException("El proyecto no existe");
        }

        // Verificar si la tecnología existe
        if (!technologyRepository.existsById(command.idTecnologia())) {
            throw new IllegalStateException("La tecnología no existe");
        }

        // Verificar si la relación existe
        if (!projectTechnologyRepository.existsByIdIdProyectoAndIdIdTecnologia(
                command.idProyecto(), command.idTecnologia())) {
            throw new IllegalStateException("Esta tecnología no está asociada al proyecto");
        }

        // Eliminar la relación
        projectTechnologyRepository.deleteByIdIdProyectoAndIdIdTecnologia(
                command.idProyecto(), command.idTecnologia());
        
        return true;
    }
} 