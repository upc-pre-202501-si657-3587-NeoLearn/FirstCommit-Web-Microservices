package com.neolearn.projects_service.project.interfaces.rest.resources;

public class InviteMemberResource {
    private Long idUsuarioInvitado;

    public InviteMemberResource() {
    }

    public InviteMemberResource(Long idUsuarioInvitado) {
        this.idUsuarioInvitado = idUsuarioInvitado;
    }

    public Long getIdUsuarioInvitado() {
        return idUsuarioInvitado;
    }

    public void setIdUsuarioInvitado(Long idUsuarioInvitado) {
        this.idUsuarioInvitado = idUsuarioInvitado;
    }
} 