package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.Task;
import com.neolearn.projects_service.project.interfaces.rest.resources.TaskResource;

public class TaskResourceFromEntityAssembler {
    
    public static TaskResource toResourceFromEntity(Task entity) {
        return new TaskResource(
                entity.getId(),
                entity.getIdProyecto(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getIdUsuarioAsignado(),
                entity.getFechaCreacion(),
                entity.getFechaVencimiento(),
                entity.getEstado()
        );
    }
} 