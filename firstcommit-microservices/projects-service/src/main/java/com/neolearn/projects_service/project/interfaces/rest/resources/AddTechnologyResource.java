package com.neolearn.projects_service.project.interfaces.rest.resources;

public class AddTechnologyResource {
    private Long idTecnologia;

    public AddTechnologyResource() {
    }

    public AddTechnologyResource(Long idTecnologia) {
        this.idTecnologia = idTecnologia;
    }

    public Long getIdTecnologia() {
        return idTecnologia;
    }

    public void setIdTecnologia(Long idTecnologia) {
        this.idTecnologia = idTecnologia;
    }
} 