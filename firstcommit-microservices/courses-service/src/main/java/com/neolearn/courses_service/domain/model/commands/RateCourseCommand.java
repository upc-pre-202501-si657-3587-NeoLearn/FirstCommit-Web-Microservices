package com.neolearn.courses_service.domain.model.commands;

public record RateCourseCommand(
        Long courseId,
        String userId,
        int rating
) {}
