package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.project.domain.model.valueobjects.TaskStatus;
import com.neolearn.projects_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Getter
@NoArgsConstructor
public class Task extends AuditableModel {

    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @Column(name = "id_usuario_asignado")
    private Long idUsuarioAsignado;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private TaskStatus estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    public Task(Long idProyecto, String nombre, String descripcion, Long idUsuarioAsignado, LocalDate fechaVencimiento) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.estado = TaskStatus.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaVencimiento = fechaVencimiento;
    }

    public void asignarUsuario(Long idUsuarioAsignado) {
        this.idUsuarioAsignado = idUsuarioAsignado;
    }

    public void updateEstado(TaskStatus estado) {
        this.estado = estado;
    }

    public void updateNombre(String nombre) {
        this.nombre = nombre;
    }

    public void updateDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void updateFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isCompletada() {
        return this.estado == TaskStatus.COMPLETADA;
    }
} 