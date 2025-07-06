package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.*;
import com.neolearn.projects_service.project.domain.model.queries.*;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.*;
import com.neolearn.projects_service.project.interfaces.rest.transform.ProjectResourceFromEntityAssembler;
import com.neolearn.projects_service.project.interfaces.rest.transform.TaskResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Projects", description = "API de Gestión de Proyectos")
public class ProjectController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;
    // UserSyncService se elimina si su única función era asegurar la existencia del usuario.
    // Esta lógica se puede mover al caso de uso (servicio de aplicación).

    public ProjectController(ProjectCommandService projectCommandService, ProjectQueryService projectQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo proyecto")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Solo usuarios autenticados pueden crear
    public ResponseEntity<ProjectResource> createProject(@RequestBody CreateProjectResource resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // ID del usuario desde el token

        // La validación de tier/límite se debe hacer en el servicio de aplicación,
        // posiblemente llamando al microservicio de Memberships.

        var command = new CreateProjectCommand(
                resource.getNombre(), resource.getDescripcionGeneral(), resource.getUrlRepositorio(),
                userId, // Usamos el ID del token
                resource.getEsPredefinido()
        );
        var project = projectCommandService.handle(command);
        return project.map(p -> new ResponseEntity<>(ProjectResourceFromEntityAssembler.toResourceFromEntity(p), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Actualizar un proyecto")
    //@PreAuthorize("@projectSecurityService.isProjectAdmin(#projectId, authentication.name)")
    public ResponseEntity<ProjectResource> updateProject(@PathVariable Long projectId, @RequestBody UpdateProjectResource resource) {
        var command = new UpdateProjectCommand(projectId, resource.getNombre(), resource.getDescripcionGeneral(), resource.getUrlRepositorio());
        var project = projectCommandService.handle(command);
        return project.map(p -> ResponseEntity.ok(ProjectResourceFromEntityAssembler.toResourceFromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Obtener un proyecto por ID")
    public ResponseEntity<ProjectResource> getProjectById(@PathVariable Long projectId) {
        var project = projectQueryService.handle(new GetProjectByIdQuery(projectId));
        return project.map(p -> ResponseEntity.ok(ProjectResourceFromEntityAssembler.toResourceFromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/platform")
    @Operation(summary = "Obtener proyectos de la plataforma")
    public ResponseEntity<List<ProjectResource>> getPlatformProjects() {
        var projects = projectQueryService.handle(new GetPlatformProjectsQuery());
        var projectResources = projects.stream().map(ProjectResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(projectResources);
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Obtener proyectos de un usuario")
   // @PreAuthorize("@projectSecurityService.canGetUserProjects(#username, authentication)")
    public ResponseEntity<List<ProjectResource>> getUserProjects(@PathVariable String username) {
        var projects = projectQueryService.handle(new GetUserProjectsQuery(username));
        var projectResources = projects.stream().map(ProjectResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(projectResources);
    }

    @PutMapping("/{projectId}/status")
    @Operation(summary = "Actualizar el estado de un proyecto")
    //@PreAuthorize("@projectSecurityService.isProjectAdmin(#projectId, authentication.name)")
    public ResponseEntity<ProjectResource> updateProjectStatus(@PathVariable Long projectId, @RequestParam("estado") String estado) {
        var command = new UpdateProjectStatusCommand(projectId, com.neolearn.projects_service.project.domain.model.valueobjects.ProjectStatus.valueOf(estado));
        var project = projectCommandService.handle(command);
        return project.map(p -> ResponseEntity.ok(ProjectResourceFromEntityAssembler.toResourceFromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{projectId}/tasks")
    @Operation(summary = "Asignar una tarea")
   // @PreAuthorize("@projectSecurityService.isProjectMember(#projectId, authentication.name)")
    public ResponseEntity<TaskResource> assignTask(@PathVariable Long projectId, @RequestBody CreateTaskResource resource) {
        var command = new AssignTaskCommand(projectId, resource.getNombre(), resource.getDescripcion(), resource.getIdUsuarioAsignado(), resource.getFechaVencimiento());
        var task = projectCommandService.handle(command);
        return task.map(t -> new ResponseEntity<>(TaskResourceFromEntityAssembler.toResourceFromEntity(t), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{projectId}/tasks")
    @Operation(summary = "Obtener tareas de un proyecto")
    // @PreAuthorize("@projectSecurityService.isProjectMember(#projectId, authentication.name)")
    public ResponseEntity<List<TaskResource>> getProjectTasks(@PathVariable Long projectId) {
        var tasks = projectQueryService.handle(new GetProjectTasksQuery(projectId));
        var taskResources = tasks.stream().map(TaskResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(taskResources);
    }

    @PutMapping("/tasks/{taskId}")
    @Operation(summary = "Actualizar una tarea")
    // Aquí la validación es más compleja. Asumimos que la lógica está en el servicio.
   // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResource> updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskResource resource) {
        // La lógica para verificar si el usuario puede modificar la tarea debería estar en el command handler
        var command = new UpdateTaskCommand(taskId, resource.getNombre(), resource.getDescripcion(), resource.getIdUsuarioAsignado(), resource.getFechaVencimiento());
        var task = projectCommandService.handle(command);
        return task.map(t -> ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/tasks/{taskId}/status")
    @Operation(summary = "Actualizar el estado de una tarea")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResource> updateTaskStatus(@PathVariable Long taskId, @RequestParam("estado") String estado) {
        var command = new UpdateTaskStatusCommand(taskId, com.neolearn.projects_service.project.domain.model.valueobjects.TaskStatus.valueOf(estado));
        var task = projectCommandService.handle(command);
        return task.map(t -> ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{projectId}/members/{username}")
    @Operation(summary = "Remover miembro del proyecto")
   // @PreAuthorize("@projectSecurityService.isProjectAdmin(#projectId, authentication.name) or #username == authentication.name")
    public ResponseEntity<Void> removeMember(@PathVariable Long projectId, @PathVariable String username) {
        // Aquí iría el comando para remover al miembro
        // projectCommandService.handle(new RemoveMemberCommand(projectId, username));
        return ResponseEntity.noContent().build();
    }
}