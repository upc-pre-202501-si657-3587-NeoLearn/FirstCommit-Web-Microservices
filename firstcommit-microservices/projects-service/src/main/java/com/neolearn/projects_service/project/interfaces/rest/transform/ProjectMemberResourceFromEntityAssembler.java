package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.ProjectMember;
import com.neolearn.projects_service.project.interfaces.rest.resources.ProjectMemberResource;

public class ProjectMemberResourceFromEntityAssembler {
    
    public static ProjectMemberResource toResourceFromEntity(ProjectMember entity) {
        return new ProjectMemberResource(
                entity.getId().getIdProyecto(),
                entity.getId().getIdUsuario(),
                entity.getRol(),
                entity.getFechaUnion()
        );
    }
} 