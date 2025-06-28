package com.neolearn.projects_service.project.domain.model.commands;

import com.neolearn.projects_service.project.domain.model.valueobjects.InvitationStatus;

public record RespondToInvitationCommand(
    Long idInvitacion,
    InvitationStatus respuesta
) {
    public RespondToInvitationCommand {
        if (idInvitacion == null || idInvitacion <= 0) {
            throw new IllegalArgumentException("El ID de la invitación debe ser válido");
        }
        if (respuesta == null) {
            throw new IllegalArgumentException("La respuesta no puede ser nula");
        }
        if (respuesta == InvitationStatus.PENDIENTE) {
            throw new IllegalArgumentException("La respuesta no puede ser PENDIENTE");
        }
    }
} 