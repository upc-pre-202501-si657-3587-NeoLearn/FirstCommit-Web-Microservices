package com.neolearn.courses_service.interfaces.rest.resources;

public record CourseResource(
        Long id,
        String title,
        String description,
        String category,
        String instructorId,
        boolean published,
        Double averageRating
) {}