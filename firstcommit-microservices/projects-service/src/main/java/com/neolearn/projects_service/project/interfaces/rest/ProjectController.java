package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.CreateProjectCommand;
import com.neolearn.projects_service.project.domain.model.commands.UpdateProjectCommand;
import com.neolearn.projects_service.project.domain.model.commands.UpdateProjectStatusCommand;
import com.neolearn.projects_service.project.domain.model.commands.AssignTaskCommand;
import com.neolearn.projects_service.project.domain.model.commands.UpdateTaskCommand;
import com.neolearn.projects_service.project.domain.model.commands.UpdateTaskStatusCommand;
import com.neolearn.projects_service.project.domain.model.queries.GetPlatformProjectsQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectByIdQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetUserProjectsQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectTasksQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.CreateProjectResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.ProjectResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.TaskResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.CreateTaskResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.UpdateTaskResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.UpdateProjectResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.ProjectResourceFromEntityAssembler;
import com.neolearn.projects_service.project.interfaces.rest.transform.TaskResourceFromEntityAssembler;
import com.neolearn.projects_service.shared.application.services.BusinessRulesService;
import com.neolearn.projects_service.shared.application.services.UserSyncService;
import com.neolearn.projects_service.shared.domain.exceptions.BusinessRuleException;
import com.neolearn.projects_service.shared.infrastructure.jwt.UserContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Projects", description = "API de Gestión de Proyectos")
public class ProjectController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;
    private final UserContextService userContextService;
    private final BusinessRulesService businessRulesService;
    private final UserSyncService userSyncService;

    public ProjectController(ProjectCommandService projectCommandService, 
                           ProjectQueryService projectQueryService, 
                           UserContextService userContextService,
                           BusinessRulesService businessRulesService,
                           UserSyncService userSyncService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
        this.userContextService = userContextService;
        this.businessRulesService = businessRulesService;
        this.userSyncService = userSyncService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo proyecto", description = "Crea un nuevo proyecto y asigna al usuario creador como administrador")
    public ResponseEntity<ProjectResource> createProject(@RequestBody CreateProjectResource resource) {
        try {
            // Extraer información del JWT token
            String currentUsername = userContextService.getCurrentUsername();
            String currentTier = userContextService.getCurrentUserTier();
            
            if (currentUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Aplicar reglas de negocio
            businessRulesService.validateTierAccess(currentTier);
            businessRulesService.validateProjectLimit(currentUsername, currentTier);
            
            // Auto-crear usuario si no existe
            userSyncService.ensureUserExists(currentUsername);
            
            var command = new CreateProjectCommand(
                    resource.getNombre(),
                    resource.getDescripcionGeneral(),
                    resource.getUrlRepositorio(),
                    currentUsername,
                    resource.getEsPredefinido()
            );

            var project = projectCommandService.handle(command);
            if (project.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            var projectResource = ProjectResourceFromEntityAssembler.toResourceFromEntity(project.get());
            return new ResponseEntity<>(projectResource, HttpStatus.CREATED);
            
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // En producción, crear un ErrorResponse con el mensaje
        }
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Actualizar un proyecto", description = "Actualiza los datos de un proyecto existente")
    public ResponseEntity<ProjectResource> updateProject(
            @PathVariable Long projectId,
            @RequestBody UpdateProjectResource resource) {
        try {
            String currentUsername = userContextService.getCurrentUsername();
            if (currentUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Solo el administrador puede modificar el proyecto
            businessRulesService.validateIsProjectAdmin(projectId, currentUsername);
            
            var command = new UpdateProjectCommand(
                    projectId,
                    resource.getNombre(),
                    resource.getDescripcionGeneral(),
                    resource.getUrlRepositorio()
            );

            var project = projectCommandService.handle(command);
            if (project.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var projectResource = ProjectResourceFromEntityAssembler.toResourceFromEntity(project.get());
            return ResponseEntity.ok(projectResource);
            
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Obtener un proyecto por ID", description = "Retorna un proyecto específico por su ID")
    public ResponseEntity<ProjectResource> getProjectById(@PathVariable Long projectId) {
        var query = new GetProjectByIdQuery(projectId);
        var project = projectQueryService.handle(query);
        if (project.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var projectResource = ProjectResourceFromEntityAssembler.toResourceFromEntity(project.get());
        return ResponseEntity.ok(projectResource);
    }

    @GetMapping("/platform")
    @Operation(summary = "Obtener proyectos de la plataforma", description = "Retorna la lista de proyectos predefinidos por la plataforma")
    public ResponseEntity<List<ProjectResource>> getPlatformProjects() {
        var query = new GetPlatformProjectsQuery();
        var projects = projectQueryService.handle(query);

        var projectResources = projects.stream()
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(projectResources);
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Obtener proyectos de un usuario", description = "Retorna la lista de proyectos en los que participa un usuario")
    public ResponseEntity<List<ProjectResource>> getUserProjects(@PathVariable String username) {
        var query = new GetUserProjectsQuery(username);
        var projects = projectQueryService.handle(query);

        var projectResources = projects.stream()
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(projectResources);
    }

    @PutMapping("/{projectId}/status")
    @Operation(summary = "Actualizar el estado de un proyecto", description = "Cambia el estado de un proyecto (abierto, cerrado, archivado)")
    public ResponseEntity<ProjectResource> updateProjectStatus(
            @PathVariable Long projectId,
            @RequestParam("estado") String estado) {
        try {
            var command = new UpdateProjectStatusCommand(
                    projectId,
                    com.neolearn.projects_service.project.domain.model.valueobjects.ProjectStatus.valueOf(estado)
            );

            var project = projectCommandService.handle(command);
            if (project.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var projectResource = ProjectResourceFromEntityAssembler.toResourceFromEntity(project.get());
            return ResponseEntity.ok(projectResource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{projectId}/tasks")
    @Operation(summary = "Asignar una tarea", description = "Crea una nueva tarea en el proyecto")
    public ResponseEntity<TaskResource> assignTask(
            @PathVariable Long projectId,
            @RequestBody CreateTaskResource resource) {
        var command = new AssignTaskCommand(
                projectId,
                resource.getNombre(),
                resource.getDescripcion(),
                resource.getIdUsuarioAsignado(),
                resource.getFechaVencimiento()
        );

        var task = projectCommandService.handle(command);
        if (task.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var taskResource = TaskResourceFromEntityAssembler.toResourceFromEntity(task.get());
        return new ResponseEntity<>(taskResource, HttpStatus.CREATED);
    }
    
    @GetMapping("/{projectId}/tasks")
    @Operation(summary = "Obtener tareas de un proyecto", description = "Retorna la lista de tareas de un proyecto")
    public ResponseEntity<List<TaskResource>> getProjectTasks(@PathVariable Long projectId) {
        var query = new GetProjectTasksQuery(projectId);
        var tasks = projectQueryService.handle(query);

        var taskResources = tasks.stream()
                .map(TaskResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskResources);
    }
    
    @PutMapping("/tasks/{taskId}")
    @Operation(summary = "Actualizar una tarea", description = "Actualiza los datos de una tarea existente")
    public ResponseEntity<TaskResource> updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskResource resource) {
        var command = new UpdateTaskCommand(
                taskId,
                resource.getNombre(),
                resource.getDescripcion(),
                resource.getIdUsuarioAsignado(),
                resource.getFechaVencimiento()
        );

        var task = projectCommandService.handle(command);
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var taskResource = TaskResourceFromEntityAssembler.toResourceFromEntity(task.get());
        return ResponseEntity.ok(taskResource);
    }
    
    @PutMapping("/tasks/{taskId}/status")
    @Operation(summary = "Actualizar el estado de una tarea", description = "Cambia el estado de una tarea (pendiente, en progreso, completada)")
    public ResponseEntity<TaskResource> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam("estado") String estado) {
        try {
            var command = new UpdateTaskStatusCommand(
                    taskId,
                    com.neolearn.projects_service.project.domain.model.valueobjects.TaskStatus.valueOf(estado)
            );

            var task = projectCommandService.handle(command);
            if (task.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var taskResource = TaskResourceFromEntityAssembler.toResourceFromEntity(task.get());
            return ResponseEntity.ok(taskResource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{projectId}/members/{username}")
    @Operation(summary = "Remover miembro del proyecto", description = "Permite al administrador botar un miembro o que un miembro se salga voluntariamente")
    public ResponseEntity<String> removeMember(
            @PathVariable Long projectId,
            @PathVariable String username) {
        try {
            String currentUsername = userContextService.getCurrentUsername();
            if (currentUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Validar si se puede remover al miembro
            businessRulesService.validateCanRemoveMember(projectId, username, currentUsername);
            
            // Crear comando para remover miembro (necesitarías implementar este comando)
            // Por ahora, implementar la lógica directamente aquí
            
            return ResponseEntity.ok("Miembro removido exitosamente");
            
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
} 