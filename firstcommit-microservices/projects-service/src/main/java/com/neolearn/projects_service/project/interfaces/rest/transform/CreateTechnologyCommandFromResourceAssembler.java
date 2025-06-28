package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.commands.CreateTechnologyCommand;
import com.neolearn.projects_service.project.interfaces.rest.resources.CreateTechnologyResource;

public class CreateTechnologyCommandFromResourceAssembler {
    public static CreateTechnologyCommand toCommandFromResource(CreateTechnologyResource resource) {
        return new CreateTechnologyCommand(resource.nombre());
    }
}