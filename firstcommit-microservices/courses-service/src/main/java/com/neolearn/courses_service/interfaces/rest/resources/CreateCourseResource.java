package com.neolearn.courses_service.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseResource(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String category,
        @NotBlank String instructorId,
        @NotNull String theoryContent,
        @NotNull String quizContent,
        @NotNull String codingContent
) {}