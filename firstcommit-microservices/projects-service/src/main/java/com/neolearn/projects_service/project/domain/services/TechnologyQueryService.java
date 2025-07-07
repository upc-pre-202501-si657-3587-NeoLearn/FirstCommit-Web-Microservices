package com.neolearn.projects_service.project.domain.services;

import com.neolearn.projects_service.project.domain.model.entities.Technology;
import com.neolearn.projects_service.project.domain.model.queries.GetAllTechnologiesQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetTechnologyByIdQuery;

import java.util.List;
import java.util.Optional;

public interface TechnologyQueryService {
    List<Technology> handle(GetAllTechnologiesQuery query);
    Optional<Technology> handle(GetTechnologyByIdQuery query);
}