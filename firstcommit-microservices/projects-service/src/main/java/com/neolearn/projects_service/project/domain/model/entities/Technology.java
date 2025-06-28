package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tecnologias")
@Getter
@NoArgsConstructor
public class Technology extends AuditableModel {

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    public Technology(String nombre) {
        this.nombre = nombre;
    }

    public void updateNombre(String nombre) {
        this.nombre = nombre;
    }
} 