package com.neolearn.courses_service.domain.model.commands;

public record UpdateCourseCommand(
        @jakarta.validation.constraints.NotNull Long courseId,
        String title,
        String description,
        String category,
        String instructorId,
        String theoryContent,  // JSON
        String quizContent,    // JSON
        String codingContent
) {}
