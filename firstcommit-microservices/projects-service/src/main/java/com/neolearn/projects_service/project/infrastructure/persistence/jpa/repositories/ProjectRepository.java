package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByEsPredefinido(Boolean esPredefinido);
    List<Project> findByUsernameCreador(String usernameCreador);
    List<Project> findByEstado(ProjectStatus estado);
    boolean existsByNombre(String nombre);
} 