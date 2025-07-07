package com.neolearn.projects_service.project.domain.model.commands;

public record RemoveMemberCommand(
    Long idProyecto,
    Long idUsuario
) {
    public RemoveMemberCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (idUsuario == null || idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser válido");
        }
    }
} 