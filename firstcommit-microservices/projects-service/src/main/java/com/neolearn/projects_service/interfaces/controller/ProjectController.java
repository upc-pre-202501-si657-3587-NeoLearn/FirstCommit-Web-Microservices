package com.neolearn.projects_service.interfaces.controller;

import com.neolearn.projects_service.application.dto.project.*;
import com.neolearn.projects_service.application.service.ProjectApplicationService;
import com.neolearn.projects_service.domain.model.ProjectType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectApplicationService projectService;
    public ProjectController(ProjectApplicationService projectService) { this.projectService = projectService; }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody CreateProjectRequest request, @RequestHeader("X-User-Id") Long creatorUserId) {
        ProjectResponse response = projectService.createProject(request, creatorUserId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable Long projectId, @RequestHeader("X-User-Id") Long currentUserId) {
        return projectService.getProjectById(projectId, currentUserId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listProjects(
            @RequestParam(required = false) ProjectType type,
            @RequestParam(name = "memberOf", required = false) Long memberUserId) {
        List<ProjectResponse> projects = projectService.getAllProjects(type, memberUserId);
        return ResponseEntity.ok(projects);
    }
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long projectId, @RequestBody UpdateProjectRequest request,
            @RequestHeader("X-User-Id") Long currentUserId) {
        ProjectResponse updatedProject = projectService.updateProjectDetails(projectId, request, currentUserId);
        return ResponseEntity.ok(updatedProject);
    }
}