package com.neolearn.projects_service.project.domain.model.commands;

public record AddTechnologyCommand(
    Long idProyecto,
    Long idTecnologia
) {
    public AddTechnologyCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (idTecnologia == null || idTecnologia <= 0) {
            throw new IllegalArgumentException("El ID de la tecnología debe ser válido");
        }
    }
} 