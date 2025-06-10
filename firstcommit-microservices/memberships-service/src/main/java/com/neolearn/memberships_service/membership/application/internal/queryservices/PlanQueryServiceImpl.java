package com.neolearn.memberships_service.membership.application.internal.queryservices;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Plan;
import com.neolearn.memberships_service.membership.domain.model.queries.GetAllPlansQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPlanByIdQuery;
import com.neolearn.memberships_service.membership.domain.services.PlanQueryService;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanQueryServiceImpl implements PlanQueryService {
    private final PlanRepository planRepository;

    public PlanQueryServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public Optional<Plan> handle(GetPlanByIdQuery query) {
        return planRepository.findById(query.id());
    }

    @Override
    public List<Plan> handle(GetAllPlansQuery query) {
        return planRepository.findAll();
    }
} 