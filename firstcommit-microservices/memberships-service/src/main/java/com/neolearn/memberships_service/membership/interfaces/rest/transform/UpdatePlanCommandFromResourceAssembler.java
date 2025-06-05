package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.UpdatePlanCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.UpdatePlanResource;

public class UpdatePlanCommandFromResourceAssembler {
    public static UpdatePlanCommand toCommandFromResource(Long planId, UpdatePlanResource resource) {
        boolean hasTrial = resource.getTrialDays() != null && resource.getTrialDays() > 0;
        
        return new UpdatePlanCommand(
            planId,
            resource.getName(),
            resource.getDescription(),
            resource.getPrice(),
            resource.getCurrency(),
            resource.getBillingPeriod(),
            hasTrial,
            resource.getTrialDays()
        );
    }
} 