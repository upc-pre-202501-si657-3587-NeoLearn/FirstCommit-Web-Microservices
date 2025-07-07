package com.neolearn.projects_service.project.domain.model.queries;

public record GetUserProjectsQuery(
    String username
) {
    public GetUserProjectsQuery {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El username del usuario debe ser válido");
        }
    }
} 