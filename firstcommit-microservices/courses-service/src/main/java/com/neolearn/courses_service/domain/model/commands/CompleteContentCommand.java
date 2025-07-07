package com.neolearn.courses_service.domain.model.commands;

public record CompleteContentCommand(
        String userId,
        Long courseId,
        String contentId) {}
