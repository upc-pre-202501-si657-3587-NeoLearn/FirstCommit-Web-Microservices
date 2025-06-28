package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.InvitationStatus;

public class RespondToInvitationResource {
    private InvitationStatus respuesta;

    public RespondToInvitationResource() {
    }

    public RespondToInvitationResource(InvitationStatus respuesta) {
        this.respuesta = respuesta;
    }

    public InvitationStatus getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(InvitationStatus respuesta) {
        this.respuesta = respuesta;
    }
} 