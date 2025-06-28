package com.neolearn.projects_service.project.domain.model.queries;

public record GetUserInvitationsQuery(
    Long idUsuario
) {
    public GetUserInvitationsQuery {
        if (idUsuario == null || idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser válido");
        }
    }
} 