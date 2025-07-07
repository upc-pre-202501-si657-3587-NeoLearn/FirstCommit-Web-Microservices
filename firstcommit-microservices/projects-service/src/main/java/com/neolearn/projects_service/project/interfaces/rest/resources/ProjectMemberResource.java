package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectRole;

import java.time.LocalDateTime;

public class ProjectMemberResource {
    private Long idProyecto;
    private Long idUsuario;
    private ProjectRole rol;
    private LocalDateTime fechaUnion;

    public ProjectMemberResource() {
    }

    public ProjectMemberResource(Long idProyecto, Long idUsuario, ProjectRole rol, LocalDateTime fechaUnion) {
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.fechaUnion = fechaUnion;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ProjectRole getRol() {
        return rol;
    }

    public void setRol(ProjectRole rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaUnion() {
        return fechaUnion;
    }

    public void setFechaUnion(LocalDateTime fechaUnion) {
        this.fechaUnion = fechaUnion;
    }
} 