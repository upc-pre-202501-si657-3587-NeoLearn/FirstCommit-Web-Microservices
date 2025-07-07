package com.neolearn.projects_service.project.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proyecto_tecnologias")
@Getter
@NoArgsConstructor
public class ProjectTechnology {

    @EmbeddedId
    private ProjectTechnologyId id;

    public ProjectTechnology(Long idProyecto, Long idTecnologia) {
        this.id = new ProjectTechnologyId(idProyecto, idTecnologia);
    }
} 