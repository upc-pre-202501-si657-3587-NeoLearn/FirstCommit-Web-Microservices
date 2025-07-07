package com.neolearn.courses_service.domain.model.queries;

public record GetUserProgressQuery(
        Long courseId,
        String  userId
) { }
