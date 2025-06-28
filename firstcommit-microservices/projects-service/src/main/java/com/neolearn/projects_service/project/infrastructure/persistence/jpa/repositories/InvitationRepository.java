package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.Invitation;
import com.neolearn.projects_service.project.domain.model.valueobjects.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByIdProyecto(Long idProyecto);
    List<Invitation> findByIdUsuarioInvitado(Long idUsuarioInvitado);
    List<Invitation> findByIdProyectoAndEstado(Long idProyecto, InvitationStatus estado);
    List<Invitation> findByIdUsuarioInvitadoAndEstado(Long idUsuarioInvitado, InvitationStatus estado);
    boolean existsByIdProyectoAndIdUsuarioInvitadoAndEstado(Long idProyecto, Long idUsuarioInvitado, InvitationStatus estado);
} 