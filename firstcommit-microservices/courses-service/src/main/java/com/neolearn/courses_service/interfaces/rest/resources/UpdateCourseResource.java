package com.neolearn.courses_service.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCourseResource(
        @NotNull Long courseId,
        @NotBlank String instructorId,
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String category,
        @NotNull String theoryContent,
        @NotNull String quizContent,
        @NotNull String codingContent
) {}