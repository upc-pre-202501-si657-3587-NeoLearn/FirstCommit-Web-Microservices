package com.neolearn.roadmaps_service.roadmaps.domain.model.commands;

public record CreateRoadmapCommand(String name, String description) {
    public CreateRoadmapCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Roadmap name is required for creation.");
        }
    }
}