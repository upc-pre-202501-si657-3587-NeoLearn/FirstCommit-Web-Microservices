package com.neolearn.courses_service.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RateCourseResource(
        @NotNull Long courseId,
        @NotNull String userId,
        @Min(1) @Max(5) Integer rating
) {}