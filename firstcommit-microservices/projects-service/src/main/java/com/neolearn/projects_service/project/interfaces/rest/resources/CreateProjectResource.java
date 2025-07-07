package com.neolearn.projects_service.project.interfaces.rest.resources;

public class CreateProjectResource {
    private String nombre;
    private String descripcionGeneral;
    private String urlRepositorio;
    private String usernameCreador;
    private Boolean esPredefinido;

    public CreateProjectResource() {
    }

    public CreateProjectResource(String nombre, String descripcionGeneral, String urlRepositorio, 
                               String usernameCreador, Boolean esPredefinido) {
        this.nombre = nombre;
        this.descripcionGeneral = descripcionGeneral;
        this.urlRepositorio = urlRepositorio;
        this.usernameCreador = usernameCreador;
        this.esPredefinido = esPredefinido;
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

    public Boolean getEsPredefinido() {
        return esPredefinido;
    }

    public void setEsPredefinido(Boolean esPredefinido) {
        this.esPredefinido = esPredefinido;
    }
} 