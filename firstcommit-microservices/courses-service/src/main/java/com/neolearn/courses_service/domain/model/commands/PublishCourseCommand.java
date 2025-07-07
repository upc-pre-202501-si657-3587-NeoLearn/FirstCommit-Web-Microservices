package com.neolearn.courses_service.domain.model.commands;

import jakarta.validation.constraints.NotNull;

public record PublishCourseCommand(
        @NotNull Long courseId,
        @NotNull String instructorId
) {}
