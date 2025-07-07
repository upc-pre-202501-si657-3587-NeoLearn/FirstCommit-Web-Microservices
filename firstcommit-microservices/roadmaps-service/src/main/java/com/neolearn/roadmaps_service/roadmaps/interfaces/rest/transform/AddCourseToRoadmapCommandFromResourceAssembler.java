package com.neolearn.roadmaps_service.roadmaps.interfaces.rest.transform;

import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.AddCourseToRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.AddCourseToRoadmapResource;

public class AddCourseToRoadmapCommandFromResourceAssembler {
    public static AddCourseToRoadmapCommand toCommandFromResource(String roadmapId, AddCourseToRoadmapResource resource) {
        return new AddCourseToRoadmapCommand(roadmapId, resource.courseId(), resource.sequence());
    }
}