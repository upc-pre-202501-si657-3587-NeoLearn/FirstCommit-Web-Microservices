package com.neolearn.roadmaps_service.roadmaps.domain.model.queries;

import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;

// Consulta para obtener un Roadmap por su ID.
public record GetRoadmapByIdQuery(RoadmapId roadmapId) {
    public GetRoadmapByIdQuery {
        if (roadmapId == null) {
            throw new IllegalArgumentException("RoadmapId cannot be null for query.");
        }
    }
}