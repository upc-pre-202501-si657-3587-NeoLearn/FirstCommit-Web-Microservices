package com.neolearn.projects_service.interfaces.controller;

import com.neolearn.projects_service.application.dto.invitation.*;
import com.neolearn.projects_service.application.dto.member.*;
import com.neolearn.projects_service.application.service.ProjectApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProjectMemberController {

    private final ProjectApplicationService projectService;
    public ProjectMemberController(ProjectApplicationService projectService) { this.projectService = projectService; }

    @PostMapping("/projects/{projectId}/invitations")
    public ResponseEntity<InvitationResponse> inviteMember(
            @PathVariable Long projectId, @RequestBody InviteMemberRequest request,
            @RequestHeader("X-User-Id") Long inviterUserId) {
        InvitationResponse invitation = projectService.inviteMemberToProject(projectId, request, inviterUserId);
        return new ResponseEntity<>(invitation, HttpStatus.CREATED);
    }
    @GetMapping("/users/{userId}/invitations/pending")
    public ResponseEntity<List<InvitationResponse>> getPendingInvitations(
            @PathVariable Long userId, @RequestHeader("X-User-Id") Long currentUserId) {
        if (!userId.equals(currentUserId)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<InvitationResponse> invitations = projectService.getPendingInvitationsForUser(userId);
        return ResponseEntity.ok(invitations);
    }
    @PostMapping("/invitations/{invitationId}/respond")
    public ResponseEntity<ProjectMemberResponse> respondToInvitation(
            @PathVariable Long invitationId, @RequestBody HandleInvitationRequest request,
            @RequestHeader("X-User-Id") Long respondingUserId) {
        ProjectMemberResponse member = projectService.respondToInvitation(invitationId, request, respondingUserId);
        return ResponseEntity.ok(member);
    }
    @DeleteMapping("/projects/{projectId}/members")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId, @RequestBody RemoveMemberRequest requestBody,
            @RequestHeader("X-User-Id") Long currentAdminUserId) {
        projectService.removeMemberFromProject(projectId, requestBody.getUserIdToRemove(), currentAdminUserId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/projects/{projectId}/members")
    public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(@PathVariable Long projectId) {
        List<ProjectMemberResponse> members = projectService.getProjectMembers(projectId);
        return ResponseEntity.ok(members);
    }
}