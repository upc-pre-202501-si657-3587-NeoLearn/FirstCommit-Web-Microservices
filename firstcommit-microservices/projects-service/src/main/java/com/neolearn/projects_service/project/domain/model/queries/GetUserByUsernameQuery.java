package com.neolearn.projects_service.project.domain.model.queries;

public record GetUserByUsernameQuery(
    String nombreUsuario
) {
    public GetUserByUsernameQuery {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        }
    }
} 