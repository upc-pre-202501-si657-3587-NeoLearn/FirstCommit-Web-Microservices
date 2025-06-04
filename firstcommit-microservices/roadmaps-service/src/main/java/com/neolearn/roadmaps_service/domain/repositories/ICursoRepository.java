package com.neolearn.roadmaps_service.domain.repositories;

import com.neolearn.roadmaps_service.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findAllByIdIn(List<Long> ids);
}
