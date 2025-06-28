package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.ProjectMember;
import com.neolearn.projects_service.project.domain.model.entities.ProjectMemberId;
import com.neolearn.projects_service.project.domain.model.valueobjects.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByIdIdProyecto(Long idProyecto);
    List<ProjectMember> findByIdIdUsuario(Long idUsuario);
    List<ProjectMember> findByIdIdProyectoAndRol(Long idProyecto, ProjectRole rol);
    Optional<ProjectMember> findByIdIdProyectoAndIdIdUsuario(Long idProyecto, Long idUsuario);
    boolean existsByIdIdProyectoAndIdIdUsuario(Long idProyecto, Long idUsuario);
    void deleteByIdIdProyectoAndIdIdUsuario(Long idProyecto, Long idUsuario);
    
    // Métodos de conteo para reglas de negocio
    long countByIdIdProyecto(Long idProyecto);
    
    @Query("SELECT COUNT(pm) FROM ProjectMember pm " +
           "JOIN User u ON pm.id.idUsuario = u.id " +
           "WHERE u.nombreUsuario = :username")
    long countProjectsByUsername(@Param("username") String username);
} 