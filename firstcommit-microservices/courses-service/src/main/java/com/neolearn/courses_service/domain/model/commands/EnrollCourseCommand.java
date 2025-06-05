package com.neolearn.courses_service.domain.model.commands;

public record EnrollCourseCommand(
        Long courseId,
        String userId
) {
    public EnrollCourseCommand {
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course ID must be a positive number.");
        }
    }
}
