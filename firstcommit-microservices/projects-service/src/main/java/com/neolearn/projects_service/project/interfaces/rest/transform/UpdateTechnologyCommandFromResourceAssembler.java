package com.neolearn.projects_service.project.interfaces.rest.transform;

import com.neolearn.projects_service.project.domain.model.commands.UpdateTechnologyCommand;
import com.neolearn.projects_service.project.interfaces.rest.resources.UpdateTechnologyResource;

public class UpdateTechnologyCommandFromResourceAssembler {
    public static UpdateTechnologyCommand toCommandFromResource(Long idTecnologia, UpdateTechnologyResource resource) {
        return new UpdateTechnologyCommand(idTecnologia, resource.nombre());
    }
}