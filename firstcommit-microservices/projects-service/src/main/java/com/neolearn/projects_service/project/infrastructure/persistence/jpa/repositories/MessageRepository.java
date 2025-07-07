package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByIdProyecto(Long idProyecto);
    
    List<Message> findByIdUsuarioEmisor(Long idUsuarioEmisor);
    
    @Query("SELECT m FROM Message m WHERE m.idProyecto = :idProyecto ORDER BY m.fechaEnvio DESC")
    List<Message> findByIdProyectoOrderByFechaEnvioDesc(@Param("idProyecto") Long idProyecto);
} 