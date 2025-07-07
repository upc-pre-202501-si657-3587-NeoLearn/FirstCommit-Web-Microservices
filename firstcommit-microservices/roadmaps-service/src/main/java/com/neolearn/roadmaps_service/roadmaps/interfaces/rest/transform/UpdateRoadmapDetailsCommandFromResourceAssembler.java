package com.neolearn.roadmaps_service.roadmaps.interfaces.rest.transform;

import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.UpdateRoadmapDetailsCommand;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.UpdateRoadmapResource;

public class UpdateRoadmapDetailsCommandFromResourceAssembler {
    public static UpdateRoadmapDetailsCommand toCommandFromResource(String roadmapId, UpdateRoadmapResource resource) {
        return new UpdateRoadmapDetailsCommand(roadmapId, resource.name(), resource.description());
    }
}