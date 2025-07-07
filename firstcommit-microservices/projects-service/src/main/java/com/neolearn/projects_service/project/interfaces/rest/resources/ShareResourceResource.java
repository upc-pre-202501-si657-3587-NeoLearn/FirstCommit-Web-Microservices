package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.ResourceType;

public class ShareResourceResource {
    private ResourceType tipoRecurso;
    private String valorRecurso;
    private String nombreVisible;

    public ShareResourceResource() {
    }

    public ShareResourceResource(ResourceType tipoRecurso, String valorRecurso, String nombreVisible) {
        this.tipoRecurso = tipoRecurso;
        this.valorRecurso = valorRecurso;
        this.nombreVisible = nombreVisible;
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
} 