package com.neolearn.courses_service.domain.model.commands;

public record DeleteCourseCommand(Long courseId, String instructorId) {
    public DeleteCourseCommand {
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course ID must be a positive number.");
        }
        if (instructorId == null || instructorId.isBlank()) {
            throw new IllegalArgumentException("Instructor ID cannot be null or empty.");
        }
    }
}
