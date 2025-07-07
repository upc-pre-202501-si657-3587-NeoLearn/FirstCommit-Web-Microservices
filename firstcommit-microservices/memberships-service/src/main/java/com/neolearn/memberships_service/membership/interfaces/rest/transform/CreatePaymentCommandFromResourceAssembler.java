package com.neolearn.memberships_service.membership.interfaces.rest.transform;



import com.neolearn.memberships_service.membership.domain.model.commands.CreatePaymentCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreatePaymentResource;

import java.time.LocalDateTime;

public class CreatePaymentCommandFromResourceAssembler {
    public static CreatePaymentCommand toCommandFromResource(CreatePaymentResource resource) {
        return new CreatePaymentCommand(
            resource.getSubscriptionId(),
            resource.getAmount(),
            resource.getCurrency(),
            LocalDateTime.now(),
            resource.getPaymentStatus(),
            resource.getPaymentMethod()
        );
    }
} 