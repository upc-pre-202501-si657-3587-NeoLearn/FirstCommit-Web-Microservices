package com.neolearn.projects_service.project.domain.model.commands;

public record UpdateProjectCommand(
    Long idProyecto,
    String nombre,
    String descripcionGeneral,
    String urlRepositorio
) {
    public UpdateProjectCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        // Los demás campos pueden ser nulos si no se van a actualizar
    }
} 