package com.neolearn.memberships_service.membership.domain.services;



import com.neolearn.memberships_service.membership.domain.model.aggregates.Plan;
import com.neolearn.memberships_service.membership.domain.model.queries.GetAllPlansQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPlanByIdQuery;

import java.util.List;
import java.util.Optional;

public interface PlanQueryService {
    Optional<Plan> handle(GetPlanByIdQuery query);
    List<Plan> handle(GetAllPlansQuery query);
} 