package com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects;

public record CourseId(String id) {
    public CourseId {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("CourseId cannot be null or blank");
        }
    }

    public static CourseId from(String id) {
        return new CourseId(id);
    }
}