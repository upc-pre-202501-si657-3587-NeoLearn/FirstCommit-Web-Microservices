package com.neolearn.projects_service.project.domain.services;

import com.neolearn.projects_service.project.domain.model.commands.CreateTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.DeleteTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.UpdateTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.entities.Technology;

import java.util.Optional;

public interface TechnologyCommandService {
    Optional<Technology> handle(CreateTechnologyCommand command);
    Optional<Technology> handle(UpdateTechnologyCommand command);
    boolean handle(DeleteTechnologyCommand command);
}