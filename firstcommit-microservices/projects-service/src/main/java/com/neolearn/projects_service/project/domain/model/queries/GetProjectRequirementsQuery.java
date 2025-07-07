package com.neolearn.projects_service.project.domain.model.queries;

import com.neolearn.projects_service.project.domain.model.valueobjects.RequirementType;

public record GetProjectRequirementsQuery(
    Long idProyecto,
    RequirementType tipo
) {
    public GetProjectRequirementsQuery {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        // tipo puede ser nulo si se quieren obtener todos los requerimientos
    }
} 