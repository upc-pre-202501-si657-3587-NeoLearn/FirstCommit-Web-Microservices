package com.neolearn.roadmaps_service.roadmaps.interfaces.rest;

import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.DeleteRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.RemoveCourseFromRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.queries.GetAllRoadmapsQuery;
import com.neolearn.roadmaps_service.roadmaps.domain.model.queries.GetRoadmapByIdQuery;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapCommandService;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapQueryService;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.AddCourseToRoadmapResource;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.CreateRoadmapResource;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.RoadmapResource;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.UpdateRoadmapResource;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.transform.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roadmaps")
public class RoadmapController {

    private final RoadmapCommandService roadmapCommandService;
    private final RoadmapQueryService roadmapQueryService;

    public RoadmapController(RoadmapCommandService roadmapCommandService, RoadmapQueryService roadmapQueryService) {
        // Inyecta los servicios de comandos y consultas.
        this.roadmapCommandService = roadmapCommandService;
        this.roadmapQueryService = roadmapQueryService;
    }

    /**
     * Crea un nuevo Roadmap.
     * @param resource los datos del roadmap a crear.
     * @return el roadmap creado con estado 201.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoadmapResource> createRoadmap(@RequestBody CreateRoadmapResource resource) {
        var command = CreateRoadmapCommandFromResourceAssembler.toCommandFromResource(resource);
        var roadmapId = roadmapCommandService.handle(command);
        var roadmap = roadmapQueryService.handle(new GetRoadmapByIdQuery(roadmapId))
                .orElseThrow(() -> new IllegalStateException("Roadmap could not be found after creation"));

        var roadmapResource = RoadmapResourceFromEntityAssembler.toResourceFromEntity(roadmap);
        return new ResponseEntity<>(roadmapResource, HttpStatus.CREATED);
    }

    /**
     * Obtiene un roadmap por su ID.
     * @param roadmapId el ID del roadmap.
     * @return el roadmap encontrado o 404 si no existe.
     */
    @GetMapping("/{roadmapId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoadmapResource> getRoadmapById(@PathVariable String roadmapId) {
        var query = new GetRoadmapByIdQuery(RoadmapId.from(roadmapId));
        return roadmapQueryService.handle(query)
                .map(entity -> ResponseEntity.ok(RoadmapResourceFromEntityAssembler.toResourceFromEntity(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los roadmaps.
     * @return una lista de todos los roadmaps.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoadmapResource>> getAllRoadmaps() {
        var query = new GetAllRoadmapsQuery();
        var roadmaps = roadmapQueryService.handle(query);
        var roadmapResources = roadmaps.stream()
                .map(RoadmapResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roadmapResources);
    }

    /**
     * Actualiza los detalles de un roadmap existente.
     * @param roadmapId el ID del roadmap a actualizar.
     * @param resource los nuevos datos del roadmap.
     * @return el roadmap actualizado.
     */
    @PutMapping("/{roadmapId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoadmapResource> updateRoadmap(@PathVariable String roadmapId, @RequestBody UpdateRoadmapResource resource) {
        var command = UpdateRoadmapDetailsCommandFromResourceAssembler.toCommandFromResource(roadmapId, resource);

         roadmapCommandService.handle(command);

        var updatedRoadmap = roadmapQueryService.handle(new GetRoadmapByIdQuery(RoadmapId.from(roadmapId)))
                .orElseThrow(() -> new IllegalStateException("Roadmap could not be found after update"));

        var roadmapResource = RoadmapResourceFromEntityAssembler.toResourceFromEntity(updatedRoadmap);
        return ResponseEntity.ok(roadmapResource);
    }

    /**
     * Elimina un roadmap por completo.
     * @param roadmapId el ID del roadmap a eliminar.
     * @return estado 204 No Content.
     */
    @DeleteMapping("/{roadmapId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoadmap(@PathVariable String roadmapId) {
        var command = new DeleteRoadmapCommand(roadmapId);
        roadmapCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }

    /**
     * Añade un curso a un roadmap existente.
     * @param roadmapId el ID del roadmap.
     * @param resource los datos del curso a añadir.
     * @return estado 201 Created.
     */
    @PostMapping("/{roadmapId}/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addCourseToRoadmap(@PathVariable String roadmapId, @RequestBody AddCourseToRoadmapResource resource) {
        var command = AddCourseToRoadmapCommandFromResourceAssembler.toCommandFromResource(roadmapId, resource);
        roadmapCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Elimina un curso de un roadmap.
     * @param roadmapId el ID del roadmap.
     * @param courseId el ID del curso a eliminar.
     * @return estado 204 No Content.
     */
    @DeleteMapping("/{roadmapId}/courses/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeCourseFromRoadmap(@PathVariable String roadmapId, @PathVariable String courseId) {
        var command = new RemoveCourseFromRoadmapCommand(roadmapId, courseId);
        roadmapCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}