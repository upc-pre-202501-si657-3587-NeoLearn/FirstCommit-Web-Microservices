package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.Task;
import com.neolearn.projects_service.project.domain.model.valueobjects.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByIdProyecto(Long idProyecto);
    List<Task> findByIdUsuarioAsignado(Long idUsuarioAsignado);
    List<Task> findByIdProyectoAndIdUsuarioAsignado(Long idProyecto, Long idUsuarioAsignado);
    List<Task> findByIdProyectoAndEstado(Long idProyecto, TaskStatus estado);
    List<Task> findByIdUsuarioAsignadoAndEstado(Long idUsuarioAsignado, TaskStatus estado);
} 