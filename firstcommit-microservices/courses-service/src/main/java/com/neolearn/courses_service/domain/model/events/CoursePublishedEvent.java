package com.neolearn.courses_service.domain.model.events;
import java.time.Instant;

public record CoursePublishedEvent(
        Long courseId,
        String instructorId,
        Instant publishedAt
) {
    public CoursePublishedEvent(Long courseId) {
        this(courseId, null, Instant.now());
    }
}
