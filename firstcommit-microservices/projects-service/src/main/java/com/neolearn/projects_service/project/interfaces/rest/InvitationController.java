package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.InviteMemberCommand;
import com.neolearn.projects_service.project.domain.model.commands.RespondToInvitationCommand;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectInvitationsQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetUserInvitationsQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.InvitationResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.InviteMemberResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.RespondToInvitationResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.InvitationResourceFromEntityAssembler;
import com.neolearn.projects_service.shared.application.services.BusinessRulesService;
import com.neolearn.projects_service.shared.application.services.UserSyncService;
import com.neolearn.projects_service.shared.domain.exceptions.BusinessRuleException;
import com.neolearn.projects_service.shared.infrastructure.jwt.UserContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Invitations", description = "API de Gestión de Invitaciones a Proyectos")
public class InvitationController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;
    private final UserContextService userContextService;
    private final BusinessRulesService businessRulesService;
    private final UserSyncService userSyncService;

    public InvitationController(ProjectCommandService projectCommandService, 
                              ProjectQueryService projectQueryService,
                              UserContextService userContextService,
                              BusinessRulesService businessRulesService,
                              UserSyncService userSyncService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
        this.userContextService = userContextService;
        this.businessRulesService = businessRulesService;
        this.userSyncService = userSyncService;
    }

    @PostMapping("/api/v1/projects/{projectId}/invitations")
    @Operation(summary = "Invitar a un usuario", description = "Invita a un usuario a unirse a un proyecto")
    public ResponseEntity<InvitationResource> inviteMember(
            @PathVariable Long projectId,
            @RequestBody InviteMemberResource resource) {
        try {
            String currentUsername = userContextService.getCurrentUsername();
            if (currentUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Solo el administrador puede invitar miembros
            businessRulesService.validateIsProjectAdmin(projectId, currentUsername);
            
            // Validar límite de miembros del proyecto
            businessRulesService.validateMemberLimit(projectId);

            // Auto-crear usuario invitador si no existe
            userSyncService.ensureUserExists(currentUsername);

            var command = new InviteMemberCommand(
                    projectId,
                    resource.getIdUsuarioInvitado(),
                    userContextService.getCurrentUserId() // Usar ID del JWT
            );

            var invitation = projectCommandService.handle(command);
            if (invitation.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get());
            return new ResponseEntity<>(invitationResource, HttpStatus.CREATED);
            
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/api/v1/projects/{projectId}/invitations")
    @Operation(summary = "Obtener invitaciones de un proyecto", description = "Retorna la lista de invitaciones de un proyecto")
    public ResponseEntity<List<InvitationResource>> getProjectInvitations(@PathVariable Long projectId) {
        var query = new GetProjectInvitationsQuery(projectId);
        var invitations = projectQueryService.handle(query);

        var invitationResources = invitations.stream()
                .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(invitationResources);
    }

    @GetMapping("/api/v1/users/{userId}/invitations")
    @Operation(summary = "Obtener invitaciones de un usuario", description = "Retorna la lista de invitaciones recibidas por un usuario")
    public ResponseEntity<List<InvitationResource>> getUserInvitations(@PathVariable Long userId) {
        var query = new GetUserInvitationsQuery(userId);
        var invitations = projectQueryService.handle(query);

        var invitationResources = invitations.stream()
                .map(InvitationResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(invitationResources);
    }

    @PutMapping("/api/v1/invitations/{invitationId}/respond")
    @Operation(summary = "Responder a una invitación", description = "Acepta o rechaza una invitación a un proyecto")
    public ResponseEntity<InvitationResource> respondToInvitation(
            @PathVariable Long invitationId,
            @RequestBody RespondToInvitationResource resource) {
        try {
            String currentUsername = userContextService.getCurrentUsername();
            String currentTier = userContextService.getCurrentUserTier();
            
            if (currentUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Si acepta la invitación, validar que puede unirse a más proyectos
            if ("ACEPTADA".equals(resource.getRespuesta().name())) {
                businessRulesService.validateCanJoinProject(currentUsername, currentTier);
                // Auto-crear usuario si no existe
                userSyncService.ensureUserExists(currentUsername);
            }

            var command = new RespondToInvitationCommand(
                    invitationId,
                    resource.getRespuesta()
            );

            var invitation = projectCommandService.handle(command);
            if (invitation.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get());
            return ResponseEntity.ok(invitationResource);
            
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
} 