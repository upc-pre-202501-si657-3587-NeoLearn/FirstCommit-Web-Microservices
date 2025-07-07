package com.neolearn.projects_service.project.domain.model.commands;

public record InviteMemberCommand(
    Long idProyecto,
    Long idUsuarioInvitado,
    Long idUsuarioInvitador
) {
    public InviteMemberCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (idUsuarioInvitado == null || idUsuarioInvitado <= 0) {
            throw new IllegalArgumentException("El ID del usuario invitado debe ser válido");
        }
        if (idUsuarioInvitador == null || idUsuarioInvitador <= 0) {
            throw new IllegalArgumentException("El ID del usuario invitador debe ser válido");
        }
    }
} 