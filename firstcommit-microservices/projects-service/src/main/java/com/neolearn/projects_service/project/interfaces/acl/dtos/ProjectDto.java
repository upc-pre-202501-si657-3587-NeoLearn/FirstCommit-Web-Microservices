package com.neolearn.projects_service.project.interfaces.acl.dtos;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectStatus;

public class ProjectDto {
    private final Long id;
    private final String nombre;
    private final String descripcionGeneral;
    private final ProjectStatus estado;
    private final String usernameCreador;

    public ProjectDto(Project project) {
        this.id = project.getId();
        this.nombre = project.getNombre();
        this.descripcionGeneral = project.getDescripcionGeneral();
        this.estado = project.getEstado();
        this.usernameCreador = project.getUsernameCreador();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public ProjectStatus getEstado() {
        return estado;
    }

    public String getUsernameCreador() {
        return usernameCreador;
    }
} 