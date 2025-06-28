package com.neolearn.projects_service.project.interfaces.rest.resources;

public class SendMessageResource {
    private String contenido;

    public SendMessageResource() {
    }

    public SendMessageResource(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
} 