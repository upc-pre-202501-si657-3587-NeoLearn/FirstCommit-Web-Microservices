package com.neolearn.roadmaps_service.roadmaps.domain.model.commands;

public record AddCourseToRoadmapCommand(String roadmapId, String courseId, int sequence) {
    public AddCourseToRoadmapCommand {
        if (roadmapId == null || roadmapId.isBlank() || courseId == null || courseId.isBlank()) {
            throw new IllegalArgumentException("RoadmapId and CourseId are required.");
        }
    }
}