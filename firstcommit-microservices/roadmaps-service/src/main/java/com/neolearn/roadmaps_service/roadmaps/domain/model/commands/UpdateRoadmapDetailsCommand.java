package com.neolearn.roadmaps_service.roadmaps.domain.model.commands;

public record UpdateRoadmapDetailsCommand(String roadmapId, String name, String description) {
    public UpdateRoadmapDetailsCommand {
        if (roadmapId == null || roadmapId.isBlank()) {
            throw new IllegalArgumentException("RoadmapId is required for update.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Roadmap name cannot be blank.");
        }
    }
}