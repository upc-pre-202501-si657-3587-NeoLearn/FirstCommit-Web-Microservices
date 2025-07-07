package com.neolearn.projects_service.project.domain.model.queries;

public record GetProjectByIdQuery(
    Long id
) {
    public GetProjectByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
    }
} 