package com.neolearn.projects_service.project.domain.model.queries;

public record GetUserByIdQuery(
    Long id
) {
    public GetUserByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser válido");
        }
    }
} 