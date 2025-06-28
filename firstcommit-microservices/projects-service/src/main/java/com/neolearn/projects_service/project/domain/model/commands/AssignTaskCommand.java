package com.neolearn.projects_service.project.domain.model.commands;

import java.time.LocalDate;

public record AssignTaskCommand(
    Long idProyecto,
    String nombre,
    String descripcion,
    Long idUsuarioAsignado,
    LocalDate fechaVencimiento
) {
    public AssignTaskCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarea no puede ser nulo o vacío");
        }
    }
} 