package com.neolearn.projects_service.project.domain.model.commands;

import com.neolearn.projects_service.project.domain.model.valueobjects.RequirementType;

public record AddRequirementCommand(
    Long idProyecto,
    String descripcion,
    RequirementType tipo
) {
    public AddRequirementCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción del requerimiento no puede ser nula o vacía");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de requerimiento no puede ser nulo");
        }
    }
} 