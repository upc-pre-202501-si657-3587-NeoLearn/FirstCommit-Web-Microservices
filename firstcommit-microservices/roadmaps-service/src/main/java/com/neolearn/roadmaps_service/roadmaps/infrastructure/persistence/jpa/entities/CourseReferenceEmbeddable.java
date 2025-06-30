package com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data // Genera getters, setters, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
public class CourseReferenceEmbeddable {

    @Column(name = "course_id", nullable = false)
    private String courseId;

    @Column(nullable = false)
    private int sequence;
}