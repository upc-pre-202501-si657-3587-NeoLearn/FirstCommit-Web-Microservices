package com.neolearn.projects_service.project.application.internal.queryservices;

import com.neolearn.projects_service.project.domain.model.entities.Technology;
import com.neolearn.projects_service.project.domain.model.queries.GetAllTechnologiesQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetTechnologyByIdQuery;
import com.neolearn.projects_service.project.domain.services.TechnologyQueryService;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.TechnologyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TechnologyQueryServiceImpl implements TechnologyQueryService {

    private final TechnologyRepository technologyRepository;

    public TechnologyQueryServiceImpl(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Override
    public List<Technology> handle(GetAllTechnologiesQuery query) {
        return technologyRepository.findAll();
    }

    @Override
    public Optional<Technology> handle(GetTechnologyByIdQuery query) {
        return technologyRepository.findById(query.idTecnologia());
    }
}