package com.neolearn.projects_service.project.interfaces.rest.resources;

import java.time.LocalDate;

public class UpdateTaskResource {
    private String nombre;
    private String descripcion;
    private Long idUsuarioAsignado;
    private LocalDate fechaVencimiento;

    public UpdateTaskResource() {
    }

    public UpdateTaskResource(String nombre, String descripcion, Long idUsuarioAsignado, LocalDate fechaVencimiento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdUsuarioAsignado() {
        return idUsuarioAsignado;
    }

    public void setIdUsuarioAsignado(Long idUsuarioAsignado) {
        this.idUsuarioAsignado = idUsuarioAsignado;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
} 