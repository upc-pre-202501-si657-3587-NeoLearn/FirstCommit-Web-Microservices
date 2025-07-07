package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.AddTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.CreateTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.DeleteTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.RemoveTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.UpdateTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.queries.GetAllTechnologiesQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectTechnologiesQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetTechnologyByIdQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.domain.services.TechnologyCommandService;
import com.neolearn.projects_service.project.domain.services.TechnologyQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.TechnologyResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.AddTechnologyResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.CreateTechnologyResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.UpdateTechnologyResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.CreateTechnologyCommandFromResourceAssembler;
import com.neolearn.projects_service.project.interfaces.rest.transform.TechnologyResourceFromEntityAssembler;
import com.neolearn.projects_service.project.interfaces.rest.transform.UpdateTechnologyCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Technologies", description = "API de Gestión de Tecnologías")
public class TechnologyController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;
    private final TechnologyCommandService technologyCommandService;
    private final TechnologyQueryService technologyQueryService;

    public TechnologyController(ProjectCommandService projectCommandService, 
                               ProjectQueryService projectQueryService,
                               TechnologyCommandService technologyCommandService,
                               TechnologyQueryService technologyQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
        this.technologyCommandService = technologyCommandService;
        this.technologyQueryService = technologyQueryService;
    }


    @PostMapping(value = "/api/v1/technologies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear tecnología", description = "Crea una nueva tecnología")
    public ResponseEntity<TechnologyResource> createTechnology(@Valid @RequestBody CreateTechnologyResource resource) {
        var command = CreateTechnologyCommandFromResourceAssembler.toCommandFromResource(resource);
        var technology = technologyCommandService.handle(command);
        
        if (technology.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var technologyResource = TechnologyResourceFromEntityAssembler.toResourceFromEntity(technology.get());
        return new ResponseEntity<>(technologyResource, HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/v1/technologies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar todas las tecnologías", description = "Obtiene la lista de todas las tecnologías disponibles")
    public ResponseEntity<List<TechnologyResource>> getAllTechnologies() {
        var query = new GetAllTechnologiesQuery();
        var technologies = technologyQueryService.handle(query);
        
        var technologyResources = technologies.stream()
                .map(TechnologyResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(technologyResources);
    }

    @GetMapping(value = "/api/v1/technologies/{technologyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener tecnología por ID", description = "Obtiene una tecnología específica por su ID")
    public ResponseEntity<TechnologyResource> getTechnologyById(@PathVariable Long technologyId) {
        var query = new GetTechnologyByIdQuery(technologyId);
        var technology = technologyQueryService.handle(query);
        
        if (technology.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var technologyResource = TechnologyResourceFromEntityAssembler.toResourceFromEntity(technology.get());
        return ResponseEntity.ok(technologyResource);
    }

    @PutMapping(value = "/api/v1/technologies/{technologyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar tecnología", description = "Actualiza una tecnología existente")
    public ResponseEntity<TechnologyResource> updateTechnology(
            @PathVariable Long technologyId, 
            @Valid @RequestBody UpdateTechnologyResource resource) {
        
        var command = UpdateTechnologyCommandFromResourceAssembler.toCommandFromResource(technologyId, resource);
        var technology = technologyCommandService.handle(command);
        
        if (technology.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var technologyResource = TechnologyResourceFromEntityAssembler.toResourceFromEntity(technology.get());
        return ResponseEntity.ok(technologyResource);
    }

    @DeleteMapping(value = "/api/v1/technologies/{technologyId}")
    @Operation(summary = "Eliminar tecnología", description = "Elimina una tecnología del sistema")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long technologyId) {
        var command = new DeleteTechnologyCommand(technologyId);
        boolean success = technologyCommandService.handle(command);
        
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/api/v1/projects/{projectId}/technologies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Añadir una tecnología", description = "Añade una tecnología al proyecto")
    public ResponseEntity<TechnologyResource> addTechnologyToProject(
            @PathVariable Long projectId,
            @RequestBody AddTechnologyResource resource) {

        var command = new AddTechnologyCommand(
                projectId,
                resource.getIdTecnologia()
        );

        var projectTechnology = projectCommandService.handle(command);
        if (projectTechnology.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var query = new GetProjectTechnologiesQuery(projectId);
        var technologies = projectQueryService.handle(query);
        
        var technology = technologies.stream()
                .filter(t -> t.getId().equals(resource.getIdTecnologia()))
                .findFirst();
                
        if (technology.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        var technologyResource = TechnologyResourceFromEntityAssembler.toResourceFromEntity(technology.get());
        return new ResponseEntity<>(technologyResource, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/api/v1/projects/{projectId}/technologies/{technologyId}")
    @Operation(summary = "Eliminar una tecnología", description = "Elimina una tecnología del proyecto")
    public ResponseEntity<Void> removeTechnologyFromProject(
            @PathVariable Long projectId,
            @PathVariable Long technologyId) {

        var command = new RemoveTechnologyCommand(
                projectId,
                technologyId
        );

        boolean success = projectCommandService.handle(command);
        if (!success) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/api/v1/projects/{projectId}/technologies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener tecnologías de un proyecto", description = "Retorna la lista de tecnologías de un proyecto")
    public ResponseEntity<List<TechnologyResource>> getProjectTechnologies(@PathVariable Long projectId) {
        var query = new GetProjectTechnologiesQuery(projectId);
        var technologies = projectQueryService.handle(query);

        var technologyResources = technologies.stream()
                .map(TechnologyResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(technologyResources);
    }
} 