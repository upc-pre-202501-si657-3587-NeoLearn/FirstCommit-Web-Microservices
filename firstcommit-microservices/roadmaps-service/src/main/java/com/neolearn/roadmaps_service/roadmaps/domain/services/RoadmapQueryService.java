package com.neolearn.roadmaps_service.roadmaps.domain.services;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.queries.GetAllRoadmapsQuery;
import com.neolearn.roadmaps_service.roadmaps.domain.model.queries.GetRoadmapByIdQuery;

import java.util.List;
import java.util.Optional;

// Define los métodos para manejar cada consulta.
public interface RoadmapQueryService {
    Optional<Roadmap> handle(GetRoadmapByIdQuery query);
    List<Roadmap> handle(GetAllRoadmapsQuery query);
}