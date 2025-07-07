package com.neolearn.roadmaps_service.roadmaps.application.internal.queryservices;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.queries.GetAllRoadmapsQuery;
import com.neolearn.roadmaps_service.roadmaps.domain.model.queries.GetRoadmapByIdQuery;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapQueryService;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoadmapQueryServiceImpl implements RoadmapQueryService {

    private final RoadmapRepository roadmapRepository;

    public RoadmapQueryServiceImpl(RoadmapRepository roadmapRepository) {
        this.roadmapRepository = roadmapRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Roadmap> handle(GetRoadmapByIdQuery query) {
        return roadmapRepository.findById(query.roadmapId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Roadmap> handle(GetAllRoadmapsQuery query) {
        return roadmapRepository.findAll();
    }
}