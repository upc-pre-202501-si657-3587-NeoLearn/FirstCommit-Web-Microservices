package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.Message;
import com.neolearn.projects_service.project.interfaces.rest.resources.MessageResource;

public class MessageResourceFromEntityAssembler {
    
    public static MessageResource toResourceFromEntity(Message entity) {
        return new MessageResource(
                entity.getId(),
                entity.getIdProyecto(),
                entity.getIdUsuarioEmisor(),
                entity.getContenido(),
                entity.getFechaEnvio()
        );
    }
} 