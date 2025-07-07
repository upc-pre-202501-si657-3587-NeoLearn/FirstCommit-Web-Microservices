package com.neolearn.courses_service.interfaces.rest.resources;

import com.neolearn.courses_service.interfaces.rest.resources.CourseContentResource;

public record CourseDetailsResource(
        Long id,
        String title,
        String description,
        String category,
        String instructorId,
        String imageUrl,
        boolean published,
        Double averageRating,
        Integer userProgress, // 0-100 (null si no está inscrito)
        CourseContentResource content
) {
    public static CourseDetailsResource fromEntity(CourseDetailsResource resource) {
        return new CourseDetailsResource(
                resource.id(),
                resource.title(),
                resource.description(),
                resource.category(),
                resource.instructorId(),
                resource.imageUrl(),
                resource.published(),
                resource.averageRating(),
                resource.userProgress(),
                resource.content()
        );
    }
}