package com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects;

import java.util.UUID;

public record RoadmapId(UUID id) {
    public RoadmapId {
        if (id == null) {
            throw new IllegalArgumentException("RoadmapId cannot be null");
        }
    }

    public static RoadmapId create() {
        return new RoadmapId(UUID.randomUUID());
    }

    public static RoadmapId from(String uuid) {
        return new RoadmapId(UUID.fromString(uuid));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}