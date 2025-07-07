package com.neolearn.roadmaps_service.roadmaps.domain.model.entities;

import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.CourseId;

// Representa la referencia a un curso dentro de un Roadmap.
// La identidad principal aquí es el CourseId. También guardamos la secuencia.
public record CourseReference(CourseId courseId, int sequence) {
    public CourseReference {
        if (courseId == null) {
            throw new IllegalArgumentException("CourseId in reference cannot be null");
        }
        if (sequence < 0) {
            throw new IllegalArgumentException("Sequence cannot be negative");
        }
    }
}