package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.Requirement;
import com.neolearn.projects_service.project.interfaces.rest.resources.RequirementResource;

public class RequirementResourceFromEntityAssembler {
    
    public static RequirementResource toResourceFromEntity(Requirement entity) {
        return new RequirementResource(
                entity.getId(),
                entity.getIdProyecto(),
                entity.getDescripcion(),
                entity.getTipo()
        );
    }
} 