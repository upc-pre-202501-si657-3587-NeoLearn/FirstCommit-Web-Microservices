package com.neolearn.projects_service.project.interfaces.acl;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.domain.model.commands.CreateProjectCommand;
import com.neolearn.projects_service.project.domain.model.entities.User;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectByIdQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetUserByIdQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetUserByUsernameQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetUserProjectsQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.acl.dtos.ProjectDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProjectContextFacade {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    public ProjectContextFacade(ProjectCommandService projectCommandService, ProjectQueryService projectQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
    }

    /**
     * Crea un nuevo proyecto.
     * @param nombre Nombre del proyecto
     * @param descripcionGeneral Descripción general del proyecto
     * @param usernameCreador Username del usuario creador
     * @return ID del proyecto creado
     */
    public Long createProject(String nombre, String descripcionGeneral, String usernameCreador) {
        var command = new CreateProjectCommand(
                nombre,
                descripcionGeneral,
                null,
                usernameCreador,
                false
        );
        var result = projectCommandService.handle(command);
        return result.map(Project::getId).orElse(0L);
    }

    /**
     * Verifica si un usuario existe.
     * @param username Username del usuario
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean userExists(String username) {
        var query = new GetUserByUsernameQuery(username);
        return projectQueryService.handle(query).isPresent();
    }

    /**
     * Obtiene un proyecto por su ID.
     * @param projectId ID del proyecto
     * @return Proyecto o null si no existe
     */
    public Optional<ProjectDto> getProject(Long projectId) {
        var query = new GetProjectByIdQuery(projectId);
        return projectQueryService.handle(query).map(ProjectDto::new);
    }

    /**
     * Obtiene la lista de proyectos en los que participa un usuario.
     * @param username Username del usuario
     * @return Lista de proyectos
     */
    public List<ProjectDto> getUserProjects(String username) {
        var query = new GetUserProjectsQuery(username);
        return projectQueryService.handle(query).stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }
} 