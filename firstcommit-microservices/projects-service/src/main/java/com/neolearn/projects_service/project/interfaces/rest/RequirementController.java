package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.AddRequirementCommand;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectRequirementsQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.RequirementResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.AddRequirementResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.RequirementResourceFromEntityAssembler;
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
@Tag(name = "Requirements", description = "API de Gestión de Requerimientos de Proyectos")
public class RequirementController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    public RequirementController(ProjectCommandService projectCommandService, ProjectQueryService projectQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
    }

    @PostMapping("/{projectId}/requirements")
    @Operation(summary = "Añadir un requerimiento", description = "Añade un nuevo requerimiento al proyecto")
    public ResponseEntity<RequirementResource> addRequirement(
            @PathVariable Long projectId,
            @RequestBody AddRequirementResource resource) {

        var command = new AddRequirementCommand(
                projectId,
                resource.getDescripcion(),
                resource.getTipo()
        );

        var requirement = projectCommandService.handle(command);
        if (requirement.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var requirementResource = RequirementResourceFromEntityAssembler.toResourceFromEntity(requirement.get());
        return new ResponseEntity<>(requirementResource, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}/requirements")
    @Operation(summary = "Obtener requerimientos de un proyecto", description = "Retorna la lista de requerimientos de un proyecto")
    public ResponseEntity<List<RequirementResource>> getProjectRequirements(
            @PathVariable Long projectId,
            @RequestParam(required = false) String tipo) {
        
        var query = new GetProjectRequirementsQuery(
                projectId,
                tipo != null ? com.neolearn.projects_service.project.domain.model.valueobjects.RequirementType.valueOf(tipo) : null
        );
        
        var requirements = projectQueryService.handle(query);

        var requirementResources = requirements.stream()
                .map(RequirementResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requirementResources);
    }
} 