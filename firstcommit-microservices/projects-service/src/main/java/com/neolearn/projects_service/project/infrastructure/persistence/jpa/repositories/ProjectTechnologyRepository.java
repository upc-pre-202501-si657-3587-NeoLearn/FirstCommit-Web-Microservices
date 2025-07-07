package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.ProjectTechnology;
import com.neolearn.projects_service.project.domain.model.entities.ProjectTechnologyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTechnologyRepository extends JpaRepository<ProjectTechnology, ProjectTechnologyId> {
    List<ProjectTechnology> findByIdIdProyecto(Long idProyecto);
    List<ProjectTechnology> findByIdIdTecnologia(Long idTecnologia);
    boolean existsByIdIdProyectoAndIdIdTecnologia(Long idProyecto, Long idTecnologia);
    boolean existsByIdIdTecnologia(Long idTecnologia);
    void deleteByIdIdProyectoAndIdIdTecnologia(Long idProyecto, Long idTecnologia);
} 