package com.neolearn.projects_service.interfaces.controller;

import com.neolearn.projects_service.application.dto.task.*;
import com.neolearn.projects_service.application.service.ProjectApplicationService;
import com.neolearn.projects_service.domain.model.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
public class TaskController {

    private final ProjectApplicationService projectService;
    public TaskController(ProjectApplicationService projectService) { this.projectService = projectService; }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId, @RequestBody CreateTaskRequest request,
            @RequestHeader("X-User-Id") Long creatorUserId) {
        TaskResponse task = projectService.createTaskInProject(projectId, request, creatorUserId);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable Long projectId, @RequestParam(required = false) TaskStatus status,
            @RequestHeader("X-User-Id") Long currentUserId) {
        List<TaskResponse> tasks = projectService.getTasksForProject(projectId, currentUserId, status);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long projectId, @PathVariable Long taskId,
            @RequestHeader("X-User-Id") Long currentUserId) {
        return projectService.getTaskById(projectId, taskId, currentUserId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long projectId, @PathVariable Long taskId,
            @RequestBody UpdateTaskRequest request, @RequestHeader("X-User-Id") Long currentUserId) {
        TaskResponse updatedTask = projectService.updateTask(projectId, taskId, request, currentUserId);
        return ResponseEntity.ok(updatedTask);
    }
    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long projectId, @PathVariable Long taskId,
            @RequestBody AssignTaskRequest request, @RequestHeader("X-User-Id") Long assignerUserId) {
        TaskResponse task = projectService.assignTaskToMember(projectId, taskId, request, assignerUserId);
        return ResponseEntity.ok(task);
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId, @PathVariable Long taskId,
            @RequestHeader("X-User-Id") Long currentUserId) {
        projectService.deleteTask(projectId, taskId, currentUserId);
        return ResponseEntity.noContent().build();
    }
}