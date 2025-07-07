package com.neolearn.projects_service.project.domain.model.queries;

public record GetUserByEmailQuery(
    String email
) {
    public GetUserByEmailQuery {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
    }
} 