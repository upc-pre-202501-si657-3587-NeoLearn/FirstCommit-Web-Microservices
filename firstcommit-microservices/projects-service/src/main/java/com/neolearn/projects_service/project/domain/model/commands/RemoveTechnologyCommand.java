package com.neolearn.projects_service.project.domain.model.commands;

public record RemoveTechnologyCommand(
    Long idProyecto,
    Long idTecnologia
) {
    public RemoveTechnologyCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (idTecnologia == null || idTecnologia <= 0) {
            throw new IllegalArgumentException("El ID de la tecnología debe ser válido");
        }
    }
} 