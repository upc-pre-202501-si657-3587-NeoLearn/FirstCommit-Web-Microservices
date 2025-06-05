package com.neolearn.courses_service.domain.model.queries;

public record GetCourseDetailsQuery(
        Long courseId,
        String  userId
) {
}
