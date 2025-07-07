package com.neolearn.projects_service.project.interfaces.rest.resources;

import java.time.LocalDateTime;

public class MessageResource {
    private Long id;
    private Long idProyecto;
    private Long idUsuarioEmisor;
    private String contenido;
    private LocalDateTime fechaEnvio;

    public MessageResource() {
    }

    public MessageResource(Long id, Long idProyecto, Long idUsuarioEmisor, String contenido, LocalDateTime fechaEnvio) {
        this.id = id;
        this.idProyecto = idProyecto;
        this.idUsuarioEmisor = idUsuarioEmisor;
        this.contenido = contenido;
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

    public Long getIdUsuarioEmisor() {
        return idUsuarioEmisor;
    }

    public void setIdUsuarioEmisor(Long idUsuarioEmisor) {
        this.idUsuarioEmisor = idUsuarioEmisor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
} 