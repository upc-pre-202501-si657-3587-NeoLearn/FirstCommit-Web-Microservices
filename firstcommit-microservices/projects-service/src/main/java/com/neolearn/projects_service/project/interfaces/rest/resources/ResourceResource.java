package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.ResourceType;

import java.time.LocalDateTime;

public class ResourceResource {
    private Long id;
    private Long idProyecto;
    private Long idUsuarioQueComparte;
    private ResourceType tipoRecurso;
    private String valorRecurso;
    private String nombreVisible;
    private LocalDateTime fechaCompartido;

    public ResourceResource() {
    }

    public ResourceResource(Long id, Long idProyecto, Long idUsuarioQueComparte, ResourceType tipoRecurso,
                           String valorRecurso, String nombreVisible, LocalDateTime fechaCompartido) {
        this.id = id;
        this.idProyecto = idProyecto;
        this.idUsuarioQueComparte = idUsuarioQueComparte;
        this.tipoRecurso = tipoRecurso;
        this.valorRecurso = valorRecurso;
        this.nombreVisible = nombreVisible;
        this.fechaCompartido = fechaCompartido;
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

    public Long getIdUsuarioQueComparte() {
        return idUsuarioQueComparte;
    }

    public void setIdUsuarioQueComparte(Long idUsuarioQueComparte) {
        this.idUsuarioQueComparte = idUsuarioQueComparte;
    }

    public ResourceType getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(ResourceType tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public String getValorRecurso() {
        return valorRecurso;
    }

    public void setValorRecurso(String valorRecurso) {
        this.valorRecurso = valorRecurso;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }

    public void setNombreVisible(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public LocalDateTime getFechaCompartido() {
        return fechaCompartido;
    }

    public void setFechaCompartido(LocalDateTime fechaCompartido) {
        this.fechaCompartido = fechaCompartido;
    }
} 