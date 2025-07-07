package com.neolearn.courses_service.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record EnrollCourseResource(
        @NotNull Long courseId,
        @NotNull String userId
) {}