package com.neolearn.projects_service.interfaces.controller;

import com.neolearn.projects_service.application.dto.resource.*;
import com.neolearn.projects_service.application.service.ProjectApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/resources")
public class ResourceController {

    private final ProjectApplicationService projectService;
    public ResourceController(ProjectApplicationService projectService) { this.projectService = projectService; }

    @PostMapping
    public ResponseEntity<ResourceResponse> addResource(
            @PathVariable Long projectId, @RequestBody CreateResourceRequest request,
            @RequestHeader("X-User-Id") Long uploaderUserId) {
        ResourceResponse resource = projectService.addResourceToProject(projectId, request, uploaderUserId);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getResources(
            @PathVariable Long projectId, @RequestHeader("X-User-Id") Long currentUserId) {
        List<ResourceResponse> resources = projectService.getProjectResources(projectId, currentUserId);
        return ResponseEntity.ok(resources);
    }
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Long projectId, @PathVariable Long resourceId,
            @RequestHeader("X-User-Id") Long currentUserId) {
        projectService.deleteResource(projectId, resourceId, currentUserId);
        return ResponseEntity.noContent().build();
    }
}