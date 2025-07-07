package com.neolearn.common_events.events;

// Usamos un record para un DTO inmutable y conciso
public record CourseCreatedEvent(
        String courseId,
        String title,
        String summary
) {}