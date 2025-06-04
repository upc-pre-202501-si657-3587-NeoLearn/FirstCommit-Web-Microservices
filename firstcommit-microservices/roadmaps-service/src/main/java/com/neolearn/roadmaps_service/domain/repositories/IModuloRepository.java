package com.neolearn.roadmaps_service.domain.repositories;

import com.neolearn.roadmaps_service.domain.model.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IModuloRepository extends JpaRepository<Modulo, Long> {
}
