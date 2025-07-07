package com.neolearn.projects_service.project.domain.model.commands;

public record CreateUserCommand(
    Long id,
    String nombreUsuario,
    String email
) {
    public CreateUserCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser válido");
        }
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
    }
} 