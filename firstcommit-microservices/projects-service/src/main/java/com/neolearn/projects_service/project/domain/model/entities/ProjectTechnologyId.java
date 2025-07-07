package com.neolearn.projects_service.project.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTechnologyId implements Serializable {

    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @Column(name = "id_tecnologia", nullable = false)
    private Long idTecnologia;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectTechnologyId that = (ProjectTechnologyId) o;
        return Objects.equals(idProyecto, that.idProyecto) && 
               Objects.equals(idTecnologia, that.idTecnologia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProyecto, idTecnologia);
    }
} 