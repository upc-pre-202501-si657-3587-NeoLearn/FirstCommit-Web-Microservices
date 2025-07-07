package com.neolearn.projects_service.project.domain.model.commands;

import com.neolearn.projects_service.project.domain.model.valueobjects.ResourceType;

public record ShareResourceCommand(
    Long idProyecto,
    Long idUsuarioQueComparte,
    ResourceType tipoRecurso,
    String valorRecurso,
    String nombreVisible
) {
    public ShareResourceCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (idUsuarioQueComparte == null || idUsuarioQueComparte <= 0) {
            throw new IllegalArgumentException("El ID del usuario que comparte debe ser válido");
        }
        if (tipoRecurso == null) {
            throw new IllegalArgumentException("El tipo de recurso no puede ser nulo");
        }
        if (valorRecurso == null || valorRecurso.isBlank()) {
            throw new IllegalArgumentException("El valor del recurso no puede ser nulo o vacío");
        }
        if (nombreVisible == null || nombreVisible.isBlank()) {
            throw new IllegalArgumentException("El nombre visible no puede ser nulo o vacío");
        }
    }
} 