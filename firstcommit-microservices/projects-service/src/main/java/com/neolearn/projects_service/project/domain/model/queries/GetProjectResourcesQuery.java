package com.neolearn.projects_service.project.domain.model.queries;

public record GetProjectResourcesQuery(
    Long idProyecto
) {
    public GetProjectResourcesQuery {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
    }
} 