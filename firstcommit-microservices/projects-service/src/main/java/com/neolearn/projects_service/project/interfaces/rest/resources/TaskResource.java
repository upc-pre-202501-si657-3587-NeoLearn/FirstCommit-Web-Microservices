package com.neolearn.projects_service.project.interfaces.rest.resources;

import com.neolearn.projects_service.project.domain.model.valueobjects.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskResource {
    private Long id;
    private Long idProyecto;
    private String nombre;
    private String descripcion;
    private Long idUsuarioAsignado;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaVencimiento;
    private TaskStatus estado;

    public TaskResource() {
    }

    public TaskResource(Long id, Long idProyecto, String nombre, String descripcion, Long idUsuarioAsignado,
                       LocalDateTime fechaCreacion, LocalDate fechaVencimiento, TaskStatus estado) {
        this.id = id;
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.fechaCreacion = fechaCreacion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public TaskStatus getEstado() {
        return estado;
    }

    public void setEstado(TaskStatus estado) {
        this.estado = estado;
    }
} 