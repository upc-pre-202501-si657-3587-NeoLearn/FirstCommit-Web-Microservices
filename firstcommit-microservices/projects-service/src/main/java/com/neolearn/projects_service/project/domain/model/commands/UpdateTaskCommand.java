package com.neolearn.projects_service.project.domain.model.commands;

import java.time.LocalDate;

public record UpdateTaskCommand(
    Long idTarea,
    String nombre,
    String descripcion,
    Long idUsuarioAsignado,
    LocalDate fechaVencimiento
) {
    public UpdateTaskCommand {
        if (idTarea == null || idTarea <= 0) {
            throw new IllegalArgumentException("El ID de la tarea debe ser válido");
        }
        // Los demás campos pueden ser nulos si no se van a actualizar
    }
} 