package com.neolearn.memberships_service.membership.domain.services;



import com.neolearn.memberships_service.membership.domain.model.aggregates.Plan;
import com.neolearn.memberships_service.membership.domain.model.commands.AddBenefitToPlanCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreatePlanCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.RemoveBenefitFromPlanCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdatePlanCommand;

import java.util.Optional;

public interface PlanCommandService {
    Optional<Plan> handle(CreatePlanCommand command);
    Optional<Plan> handle(UpdatePlanCommand command);
    Optional<Plan> handle(AddBenefitToPlanCommand command);
    Optional<Plan> handle(RemoveBenefitFromPlanCommand command);
} 