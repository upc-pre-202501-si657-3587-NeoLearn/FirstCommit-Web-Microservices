package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.CreateTrialSubscriptionCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateTrialSubscriptionResource;

public class CreateTrialSubscriptionCommandFromResourceAssembler {
    public static CreateTrialSubscriptionCommand toCommandFromResource(CreateTrialSubscriptionResource resource) {
        return new CreateTrialSubscriptionCommand(
            resource.getUserId(),
            resource.getPlanId(),
            resource.getTrialDays()
        );
    }
} 