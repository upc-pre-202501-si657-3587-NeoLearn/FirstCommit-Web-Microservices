package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectStatus;

import java.time.LocalDateTime;

public class ProjectResource {
    private Long id;
    private String nombre;
    private String descripcionGeneral;
    private String urlRepositorio;
    private String usernameCreador;
    private LocalDateTime fechaCreacion;
    private Boolean esPredefinido;
    private ProjectStatus estado;

    public ProjectResource() {
    }

    public ProjectResource(Long id, String nombre, String descripcionGeneral, String urlRepositorio, 
                          String usernameCreador, LocalDateTime fechaCreacion, Boolean esPredefinido, 
                          ProjectStatus estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcionGeneral = descripcionGeneral;
        this.urlRepositorio = urlRepositorio;
        this.usernameCreador = usernameCreador;
        this.fechaCreacion = fechaCreacion;
        this.esPredefinido = esPredefinido;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public void setDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
    }

    public String getUrlRepositorio() {
        return urlRepositorio;
    }

    public void setUrlRepositorio(String urlRepositorio) {
        this.urlRepositorio = urlRepositorio;
    }

    public String getUsernameCreador() {
        return usernameCreador;
    }

    public void setUsernameCreador(String usernameCreador) {
        this.usernameCreador = usernameCreador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getEsPredefinido() {
        return esPredefinido;
    }

    public void setEsPredefinido(Boolean esPredefinido) {
        this.esPredefinido = esPredefinido;
    }

    public ProjectStatus getEstado() {
        return estado;
    }

    public void setEstado(ProjectStatus estado) {
        this.estado = estado;
    }
} 