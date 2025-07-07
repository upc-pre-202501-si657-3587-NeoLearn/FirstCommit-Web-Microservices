package com.neolearn.roadmaps_service.roadmaps.domain.model.exceptions;

import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.CourseId;

public class CourseAlreadyInRoadmapException extends RuntimeException {
    public CourseAlreadyInRoadmapException(CourseId courseId) {
        super("Course with ID " + courseId.id() + " already exists in this roadmap.");
    }
}