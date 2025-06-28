package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.project.domain.model.valueobjects.RequirementType;
import com.neolearn.projects_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requerimientos_proyecto")
@Getter
@NoArgsConstructor
public class Requirement extends AuditableModel {

    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private RequirementType tipo;

    public Requirement(Long idProyecto, String descripcion, RequirementType tipo) {
        this.idProyecto = idProyecto;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public void updateDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void updateTipo(RequirementType tipo) {
        this.tipo = tipo;
    }
} 