package com.neolearn.projects_service.project.domain.model.commands;

import com.neolearn.projects_service.project.domain.model.valueobjects.TaskStatus;

public record UpdateTaskStatusCommand(
    Long idTarea,
    TaskStatus nuevoEstado
) {
    public UpdateTaskStatusCommand {
        if (idTarea == null || idTarea <= 0) {
            throw new IllegalArgumentException("El ID de la tarea debe ser válido");
        }
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
    }
} 