package com.neolearn.projects_service.project.interfaces.rest.resources;

public class UpdateProjectResource {
    private String nombre;
    private String descripcionGeneral;
    private String urlRepositorio;

    public UpdateProjectResource() {
    }

    public UpdateProjectResource(String nombre, String descripcionGeneral, String urlRepositorio) {
        this.nombre = nombre;
        this.descripcionGeneral = descripcionGeneral;
        this.urlRepositorio = urlRepositorio;
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
} 