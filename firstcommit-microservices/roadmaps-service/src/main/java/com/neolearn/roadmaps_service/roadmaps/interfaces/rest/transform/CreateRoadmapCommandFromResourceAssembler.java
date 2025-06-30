package com.neolearn.roadmaps_service.roadmaps.interfaces.rest.transform;

import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.CreateRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources.CreateRoadmapResource;

public class CreateRoadmapCommandFromResourceAssembler {
    public static CreateRoadmapCommand toCommandFromResource(CreateRoadmapResource resource) {
        return new CreateRoadmapCommand(resource.name(), resource.description());
    }
}