package com.neolearn.roadmaps_service.roadmaps.domain.services;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;

import java.util.List;
import java.util.Optional;

public interface RoadmapRepository {
    Roadmap save(Roadmap roadmap);
    Optional<Roadmap> findById(RoadmapId roadmapId);
    List<Roadmap> findAll();
    void deleteById(RoadmapId roadmapId);
    boolean existsByName(String name);
}