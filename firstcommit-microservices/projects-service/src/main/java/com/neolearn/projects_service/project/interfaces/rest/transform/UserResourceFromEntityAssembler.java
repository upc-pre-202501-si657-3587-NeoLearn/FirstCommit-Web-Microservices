package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.User;
import com.neolearn.projects_service.project.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(
                entity.getId(),
                entity.getNombreUsuario(),
                entity.getEmail()
        );
    }
} 