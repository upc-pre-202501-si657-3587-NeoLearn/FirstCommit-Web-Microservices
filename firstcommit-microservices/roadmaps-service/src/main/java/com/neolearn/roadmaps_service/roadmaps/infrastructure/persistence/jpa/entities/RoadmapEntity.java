package com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roadmaps")
@Getter
@Setter
public class RoadmapEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roadmap_courses", joinColumns = @JoinColumn(name = "roadmap_id"))
    private Set<CourseReferenceEmbeddable> courses = new HashSet<>();
}