package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.CreatePlanCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreatePlanResource;

public class CreatePlanCommandFromResourceAssembler {
    public static CreatePlanCommand toCommandFromResource(CreatePlanResource resource) {
        boolean hasTrial = resource.getTrialDays() != null && resource.getTrialDays() > 0;
        
        return new CreatePlanCommand(
            resource.getName(),
            resource.getDescription(),
            resource.getPrice(),
            resource.getCurrency(),
            resource.getBillingPeriod(),
            hasTrial,
            resource.getTrialDays(),
            resource.getBenefitIds()
        );
    }
} 