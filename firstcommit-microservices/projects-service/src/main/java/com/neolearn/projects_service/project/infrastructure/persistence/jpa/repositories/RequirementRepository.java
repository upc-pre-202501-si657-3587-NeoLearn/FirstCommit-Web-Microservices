package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.Requirement;
import com.neolearn.projects_service.project.domain.model.valueobjects.RequirementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findByIdProyecto(Long idProyecto);
    List<Requirement> findByIdProyectoAndTipo(Long idProyecto, RequirementType tipo);
} 