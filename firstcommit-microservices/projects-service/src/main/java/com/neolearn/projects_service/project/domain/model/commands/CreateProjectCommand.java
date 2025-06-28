package com.neolearn.projects_service.project.domain.model.commands;

public record CreateProjectCommand(
    String nombre,
    String descripcionGeneral,
    String urlRepositorio,
    String usernameCreador,
    Boolean esPredefinido
) {
    public CreateProjectCommand {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede ser nulo o vacío");
        }
        if (usernameCreador == null || usernameCreador.isBlank()) {
            throw new IllegalArgumentException("El username del usuario creador debe ser válido");
        }
    }
} 