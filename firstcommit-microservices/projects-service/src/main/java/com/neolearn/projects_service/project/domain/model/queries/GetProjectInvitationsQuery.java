package com.neolearn.projects_service.project.domain.model.queries;

public record GetProjectInvitationsQuery(
    Long idProyecto
) {
    public GetProjectInvitationsQuery {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
    }
} 