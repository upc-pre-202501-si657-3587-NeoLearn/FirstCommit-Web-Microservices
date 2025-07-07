package com.neolearn.courses_service.domain.model.commands;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record CreateCourseCommand(
        String title,
        String description,
        String category,
        String instructorId,
        String theoryContent,  // JSON
        String quizContent,    // JSON
        String codingContent   // JSON
) {}