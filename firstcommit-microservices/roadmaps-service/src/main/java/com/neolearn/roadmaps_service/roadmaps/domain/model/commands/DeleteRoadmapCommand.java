package com.neolearn.roadmaps_service.roadmaps.domain.model.commands;

// Comando para eliminar un Roadmap completo.
public record DeleteRoadmapCommand(String roadmapId) {
    public DeleteRoadmapCommand {
        if (roadmapId == null || roadmapId.isBlank()) {
            throw new IllegalArgumentException("RoadmapId is required for deletion.");
        }
    }
}