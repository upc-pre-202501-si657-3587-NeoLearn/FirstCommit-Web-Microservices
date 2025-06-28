package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.entities.Invitation;
import com.neolearn.projects_service.project.interfaces.rest.resources.InvitationResource;

public class InvitationResourceFromEntityAssembler {

    public static InvitationResource toResourceFromEntity(Invitation entity) {
        return new InvitationResource(
            entity.getId(),
            entity.getIdProyecto(),
            entity.getIdUsuarioInvitado(),
            entity.getIdUsuarioInvitador(),
            entity.getEstado(),
            entity.getFechaEnvio()
        );
    }
} 