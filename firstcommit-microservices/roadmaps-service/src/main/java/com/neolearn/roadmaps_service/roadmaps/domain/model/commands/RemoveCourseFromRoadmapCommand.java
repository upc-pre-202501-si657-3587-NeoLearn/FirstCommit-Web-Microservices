package com.neolearn.roadmaps_service.roadmaps.domain.model.commands;

// Comando para eliminar un curso de un Roadmap.
public record RemoveCourseFromRoadmapCommand(String roadmapId, String courseId) {
    public RemoveCourseFromRoadmapCommand {
        if (roadmapId == null || roadmapId.isBlank() || courseId == null || courseId.isBlank()) {
            throw new IllegalArgumentException("RoadmapId and CourseId are required.");
        }
    }
}