package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.InvitationStatus;

import java.time.LocalDateTime;

public class InvitationResource {
    private Long id;
    private Long idProyecto;
    private Long idUsuarioInvitado;
    private Long idUsuarioInvitador;
    private InvitationStatus estado;
    private LocalDateTime fechaEnvio;

    public InvitationResource() {
    }

    public InvitationResource(Long id, Long idProyecto, Long idUsuarioInvitado, Long idUsuarioInvitador, 
                            InvitationStatus estado, LocalDateTime fechaEnvio) {
        this.id = id;
        this.idProyecto = idProyecto;
        this.idUsuarioInvitado = idUsuarioInvitado;
        this.idUsuarioInvitador = idUsuarioInvitador;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Long getIdUsuarioInvitado() {
        return idUsuarioInvitado;
    }

    public void setIdUsuarioInvitado(Long idUsuarioInvitado) {
        this.idUsuarioInvitado = idUsuarioInvitado;
    }

    public Long getIdUsuarioInvitador() {
        return idUsuarioInvitador;
    }

    public void setIdUsuarioInvitador(Long idUsuarioInvitador) {
        this.idUsuarioInvitador = idUsuarioInvitador;
    }

    public InvitationStatus getEstado() {
        return estado;
    }

    public void setEstado(InvitationStatus estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
} 