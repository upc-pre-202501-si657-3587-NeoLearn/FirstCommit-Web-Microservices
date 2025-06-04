package com.neolearn.projects_service.interfaces.controller;

import com.neolearn.projects_service.application.dto.chat.ProjectChatInfoResponse;
import com.neolearn.projects_service.application.service.ProjectApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/chat")
public class ChatController {

    private final ProjectApplicationService projectApplicationService;
    public ChatController(ProjectApplicationService projectApplicationService) { this.projectApplicationService = projectApplicationService; }

    @GetMapping("/info")
    public ResponseEntity<ProjectChatInfoResponse> getChatInfo(
            @PathVariable Long projectId, @RequestHeader("X-User-Id") Long userId) {
        ProjectChatInfoResponse chatInfo = projectApplicationService.getProjectChatInformation(projectId, userId);
        return ResponseEntity.ok(chatInfo);
    }
}