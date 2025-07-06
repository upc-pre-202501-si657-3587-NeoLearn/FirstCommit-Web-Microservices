package com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.repositories;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de la interfaz de Repositorio del dominio.
 * Actúa como un adaptador entre el dominio y la capa de persistencia de Spring Data JPA.
 * Es responsable de mapear entre los agregados del dominio (Roadmap) y las entidades de persistencia (RoadmapEntity).
 */
@Repository // Esta anotación la registra como un bean de Spring para inyección de dependencias.
public class RoadmapRepositoryImpl implements RoadmapRepository {

    private final SpringDataJPARoadmapRepository jpaRepository;
    private final RoadmapPersistenceMapper mapper;

    // Inyección de dependencias a través del constructor.
    public RoadmapRepositoryImpl(SpringDataJPARoadmapRepository jpaRepository, RoadmapPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Roadmap save(Roadmap roadmap) {
        var entity = mapper.toEntity(roadmap);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Roadmap> findById(RoadmapId roadmapId) {
        return jpaRepository.findById(roadmapId.id()).map(mapper::toDomain);
    }

    @Override
    public List<Roadmap> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(RoadmapId roadmapId) {
        jpaRepository.deleteById(roadmapId.id());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsById(RoadmapId roadmapId) {
        return jpaRepository.existsById(roadmapId.id());
    }
}