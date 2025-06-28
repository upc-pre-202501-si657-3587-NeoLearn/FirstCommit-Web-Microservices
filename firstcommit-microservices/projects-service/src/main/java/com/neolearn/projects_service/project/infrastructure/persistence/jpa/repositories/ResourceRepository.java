package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.Resource;
import com.neolearn.projects_service.project.domain.model.valueobjects.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByIdProyecto(Long idProyecto);
    List<Resource> findByIdProyectoAndTipoRecurso(Long idProyecto, ResourceType tipoRecurso);
    List<Resource> findByIdUsuarioQueComparte(Long idUsuarioQueComparte);
} 