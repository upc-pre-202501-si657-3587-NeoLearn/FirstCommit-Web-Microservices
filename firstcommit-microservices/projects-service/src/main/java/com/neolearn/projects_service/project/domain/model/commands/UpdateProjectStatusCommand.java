package com.neolearn.projects_service.project.domain.model.commands;

import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectStatus;

public record UpdateProjectStatusCommand(
    Long idProyecto,
    ProjectStatus nuevoEstado
) {
    public UpdateProjectStatusCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
    }
} 