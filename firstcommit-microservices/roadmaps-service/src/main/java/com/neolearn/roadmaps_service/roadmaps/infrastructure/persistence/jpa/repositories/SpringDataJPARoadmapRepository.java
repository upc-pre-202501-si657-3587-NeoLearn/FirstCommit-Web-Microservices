// Archivo: SpringDataJPARoadmapRepository.java
package com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.repositories;

import com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.entities.RoadmapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repositorio JPA interno gestionado por Spring Data.
 * Esta interfaz no se expone fuera de la capa de infraestructura.
 */
public interface SpringDataJPARoadmapRepository extends JpaRepository<RoadmapEntity, UUID> {
    boolean existsByName(String name);
}