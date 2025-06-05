package com.neolearn.courses_service.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProgressResource(
        @NotNull Long courseId,
        @NotNull String userId,
        @Min(0) @Max(100) Integer progress
) {}