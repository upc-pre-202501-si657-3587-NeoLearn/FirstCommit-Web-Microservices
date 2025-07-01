package com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.repositories;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapRepository;
import com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.entities.RoadmapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// Interfaz interna de Spring Data JPA
interface SpringDataRoadmapRepository extends JpaRepository<RoadmapEntity, UUID> {
    boolean existsByName(String name);
}

@Repository // ¡Esta anotación registra la clase como un Bean de Spring!
public class RoadmapRepositoryImpl implements RoadmapRepository {

    private final SpringDataRoadmapRepository jpaRepository;
    private final RoadmapPersistenceMapper mapper;

    public RoadmapRepositoryImpl(SpringDataRoadmapRepository jpaRepository, RoadmapPersistenceMapper mapper) {
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
}