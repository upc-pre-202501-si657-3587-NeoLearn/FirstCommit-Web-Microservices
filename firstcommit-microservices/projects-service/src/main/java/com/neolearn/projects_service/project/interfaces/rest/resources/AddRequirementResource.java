package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.RequirementType;

public class AddRequirementResource {
    private String descripcion;
    private RequirementType tipo;

    public AddRequirementResource() {
    }

    public AddRequirementResource(String descripcion, RequirementType tipo) {
        this.descripcion = descripcion;
        this.tipo = tipo;
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