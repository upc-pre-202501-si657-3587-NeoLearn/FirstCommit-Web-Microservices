package com.neolearn.projects_service.project.interfaces.rest.resources;

public class TechnologyResource {
    private Long id;
    private String nombre;

    public TechnologyResource() {
    }

    public TechnologyResource(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
} 