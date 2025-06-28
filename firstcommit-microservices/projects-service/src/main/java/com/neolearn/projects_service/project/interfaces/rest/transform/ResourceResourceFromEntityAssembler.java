package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.Resource;
import com.neolearn.projects_service.project.interfaces.rest.resources.ResourceResource;

public class ResourceResourceFromEntityAssembler {
    
    public static ResourceResource toResourceFromEntity(Resource entity) {
        return new ResourceResource(
                entity.getId(),
                entity.getIdProyecto(),
                entity.getIdUsuarioQueComparte(),
                entity.getTipoRecurso(),
                entity.getValorRecurso(),
                entity.getNombreVisible(),
                entity.getFechaCompartido()
        );
    }
} 