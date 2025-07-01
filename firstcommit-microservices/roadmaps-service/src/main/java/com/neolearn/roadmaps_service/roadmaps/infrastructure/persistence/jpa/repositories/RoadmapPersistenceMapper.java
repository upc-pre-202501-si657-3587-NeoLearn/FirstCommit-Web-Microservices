package com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.repositories;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.entities.CourseReference;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.CourseId;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;
import com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.entities.CourseReferenceEmbeddable;
import com.neolearn.roadmaps_service.roadmaps.infrastructure.persistence.jpa.entities.RoadmapEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoadmapPersistenceMapper {

    public Roadmap toDomain(RoadmapEntity entity) {
        var roadmapId = new RoadmapId(entity.getId());
        var courses = entity.getCourses().stream()
                .map(embeddable -> new CourseReference(
                        new CourseId(embeddable.getCourseId()),
                        embeddable.getSequence()
                ))
                .collect(Collectors.toList());

        return new Roadmap(roadmapId, entity.getName(), entity.getDescription(), courses);
    }

    public RoadmapEntity toEntity(Roadmap domain) {
        var entity = new RoadmapEntity();
        entity.setId(domain.getId().id());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setCourses(domain.getCourses().stream()
                .map(courseRef -> new CourseReferenceEmbeddable(
                        courseRef.courseId().id(),
                        courseRef.sequence()
                ))
                .collect(Collectors.toSet()));
        return entity;
    }
}