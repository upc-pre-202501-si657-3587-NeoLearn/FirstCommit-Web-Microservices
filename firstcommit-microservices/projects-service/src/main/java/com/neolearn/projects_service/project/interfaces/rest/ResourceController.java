package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.ShareResourceCommand;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectResourcesQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.ResourceResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.ShareResourceResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.ResourceResourceFromEntityAssembler;
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
@Tag(name = "Resources", description = "API de Gestión de Recursos Compartidos")
public class ResourceController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    public ResourceController(ProjectCommandService projectCommandService, ProjectQueryService projectQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
    }

    @PostMapping("/{projectId}/resources")
    @Operation(summary = "Compartir un recurso", description = "Comparte un recurso en el proyecto")
    public ResponseEntity<ResourceResource> shareResource(
            @PathVariable Long projectId,
            @RequestBody ShareResourceResource resource,
            @RequestHeader("X-User-Id") Long currentUserId) {

        var command = new ShareResourceCommand(
                projectId,
                currentUserId,
                resource.getTipoRecurso(),
                resource.getValorRecurso(),
                resource.getNombreVisible()
        );

        var sharedResource = projectCommandService.handle(command);
        if (sharedResource.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var resourceResource = ResourceResourceFromEntityAssembler.toResourceFromEntity(sharedResource.get());
        return new ResponseEntity<>(resourceResource, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}/resources")
    @Operation(summary = "Obtener recursos de un proyecto", description = "Retorna la lista de recursos compartidos en un proyecto")
    public ResponseEntity<List<ResourceResource>> getProjectResources(@PathVariable Long projectId) {
        var query = new GetProjectResourcesQuery(projectId);
        var resources = projectQueryService.handle(query);

        var resourceResources = resources.stream()
                .map(ResourceResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resourceResources);
    }
} 