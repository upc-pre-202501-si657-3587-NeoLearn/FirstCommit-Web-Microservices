package com.neolearn.courses_service.domain.model.commands;

public record UpdateProgressCommand(
        Long courseId,
        String userId,
        int progress  // 0-100
) {}
