package com.neolearn.roadmaps_service.roadmaps.domain.model.exceptions;

import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.CourseId;

public class CourseNotFoundInRoadmapException extends RuntimeException {
    public CourseNotFoundInRoadmapException(CourseId courseId) {
        super("Course with ID " + courseId.id() + " was not found in this roadmap.");
    }
}