package com.neolearn.projects_service.project.interfaces.rest.resources;

public class UserResource {
    private Long id;
    private String nombreUsuario;
    private String email;

    public UserResource() {
    }

    public UserResource(Long id, String nombreUsuario, String email) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 