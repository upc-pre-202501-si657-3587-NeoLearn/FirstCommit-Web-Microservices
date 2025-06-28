package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.RequirementType;

public class RequirementResource {
    private Long id;
    private Long idProyecto;
    private String descripcion;
    private RequirementType tipo;

    public RequirementResource() {
    }

    public RequirementResource(Long id, Long idProyecto, String descripcion, RequirementType tipo) {
        this.id = id;
        this.idProyecto = idProyecto;
        this.descripcion = descripcion;
        this.tipo = tipo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public RequirementType getTipo() {
        return tipo;
    }

    public void setTipo(RequirementType tipo) {
        this.tipo = tipo;
    }
} 