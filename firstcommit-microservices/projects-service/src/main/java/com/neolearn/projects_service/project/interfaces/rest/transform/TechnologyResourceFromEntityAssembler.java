package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.Technology;
import com.neolearn.projects_service.project.interfaces.rest.resources.TechnologyResource;

public class TechnologyResourceFromEntityAssembler {
    
    public static TechnologyResource toResourceFromEntity(Technology entity) {
        return new TechnologyResource(
                entity.getId(),
                entity.getNombre()
        );
    }
} 