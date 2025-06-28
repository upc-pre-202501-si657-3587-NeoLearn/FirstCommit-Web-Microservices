package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "proyecto_miembros")
@Getter
@NoArgsConstructor
public class ProjectMember {

    @EmbeddedId
    private ProjectMemberId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private ProjectRole rol;

    @Column(name = "fecha_union", nullable = false)
    private LocalDateTime fechaUnion;

    public ProjectMember(Long idProyecto, Long idUsuario, ProjectRole rol) {
        this.id = new ProjectMemberId(idProyecto, idUsuario);
        this.rol = rol;
        this.fechaUnion = LocalDateTime.now();
    }

    public void updateRol(ProjectRole rol) {
        this.rol = rol;
    }

    public boolean isAdmin() {
        return this.rol == ProjectRole.ADMINISTRADOR;
    }
} 