package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.RemoveMemberCommand;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectMembersQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.ProjectMemberResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.ProjectMemberResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Project Members", description = "API de Gestión de Miembros de Proyectos")
public class ProjectMemberController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    public ProjectMemberController(ProjectCommandService projectCommandService, ProjectQueryService projectQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
    }

    @GetMapping("/{projectId}/members")
    @Operation(summary = "Obtener miembros de un proyecto", description = "Retorna la lista de miembros de un proyecto")
    public ResponseEntity<List<ProjectMemberResource>> getProjectMembers(@PathVariable Long projectId) {
        var query = new GetProjectMembersQuery(projectId);
        var members = projectQueryService.handle(query);

        var memberResources = members.stream()
                .map(ProjectMemberResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(memberResources);
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    @Operation(summary = "Eliminar un miembro", description = "Elimina un miembro del proyecto")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        var command = new RemoveMemberCommand(
                projectId,
                userId
        );

        boolean success = projectCommandService.handle(command);
        if (!success) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
} 