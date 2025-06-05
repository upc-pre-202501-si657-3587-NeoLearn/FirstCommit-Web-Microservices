package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.CreateSubscriptionCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {
    public static CreateSubscriptionCommand toCommandFromResource(CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(
            resource.getUserId(),
            resource.getPlanId(),
            resource.getStatus()
        );
    }
} 