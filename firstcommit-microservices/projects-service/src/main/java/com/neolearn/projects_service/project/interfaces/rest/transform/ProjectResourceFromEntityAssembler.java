package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.interfaces.rest.resources.ProjectResource;

public class ProjectResourceFromEntityAssembler {

    public static ProjectResource toResourceFromEntity(Project entity) {
        return new ProjectResource(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcionGeneral(),
            entity.getUrlRepositorio(),
            entity.getUsernameCreador(),
            entity.getFechaCreacion(),
            entity.getEsPredefinido(),
            entity.getEstado()
        );
    }
} 