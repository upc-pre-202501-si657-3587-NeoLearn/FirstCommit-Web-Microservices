package com.neolearn.roadmaps_service.roadmaps.interfaces.rest.transform;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.CourseReferenceResource;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.RoadmapResource;

import java.util.stream.Collectors;

public class RoadmapResourceFromEntityAssembler {
    public static RoadmapResource toResourceFromEntity(Roadmap entity) {
        var courses = entity.getCourses().stream()
                .map(courseRef -> new CourseReferenceResource(
                        courseRef.courseId().id(),
                        courseRef.sequence()
                ))
                .collect(Collectors.toList());

        return new RoadmapResource(
                entity.getId().toString(),
                entity.getName(),
                entity.getDescription(),
                courses
        );
    }
}