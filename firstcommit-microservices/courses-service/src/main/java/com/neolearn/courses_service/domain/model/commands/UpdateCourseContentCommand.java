package com.neolearn.courses_service.domain.model.commands;

public record UpdateCourseContentCommand(
        Long courseId,
        String instructorId,  // Para validar ownership
        String theoryContent,
        String quizContent,
        String codingContent
) {}