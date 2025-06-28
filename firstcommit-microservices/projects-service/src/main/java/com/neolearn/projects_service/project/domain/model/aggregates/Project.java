package com.neolearn.projects_service.project.domain.model.aggregates;

import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectStatus;
import com.neolearn.projects_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
@Getter
@NoArgsConstructor
public class Project extends AuditableAbstractAggregateRoot<Project> {

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion_general")
    private String descripcionGeneral;

    @Column(name = "url_repositorio")
    private String urlRepositorio;

    @Column(name = "username_creador", nullable = false)
    private String usernameCreador;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "es_predefinido", nullable = false)
    private Boolean esPredefinido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private ProjectStatus estado;

    public Project(String nombre, String descripcionGeneral, String urlRepositorio, 
                   String usernameCreador, Boolean esPredefinido) {
        this.nombre = nombre;
        this.descripcionGeneral = descripcionGeneral;
        this.urlRepositorio = urlRepositorio;
        this.usernameCreador = usernameCreador;
        this.fechaCreacion = LocalDateTime.now();
        this.esPredefinido = esPredefinido != null ? esPredefinido : false;
        this.estado = ProjectStatus.ABIERTO;
    }

    public Project updateNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Project updateDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
        return this;
    }

    public Project updateUrlRepositorio(String urlRepositorio) {
        this.urlRepositorio = urlRepositorio;
        return this;
    }

    public Project updateEstado(ProjectStatus estado) {
        this.estado = estado;
        return this;
    }

    public boolean isActive() {
        return this.estado == ProjectStatus.ABIERTO;
    }
} 