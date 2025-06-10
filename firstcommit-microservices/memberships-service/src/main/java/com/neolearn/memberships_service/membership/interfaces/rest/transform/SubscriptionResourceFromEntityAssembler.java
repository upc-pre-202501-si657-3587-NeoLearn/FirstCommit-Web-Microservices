package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Subscription;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.SubscriptionResource;

public class SubscriptionResourceFromEntityAssembler {

    public static SubscriptionResource toResourceFromEntity(Subscription entity) {
        return new SubscriptionResource(
            entity.getId(),
            entity.getUserId(),
            entity.getPlanId(),
            entity.getStatus(),
            entity.isActive(),
            entity.isInTrial(),
            entity.getStartDate(),
            entity.getRenewalDate(),
            entity.getEndDate(),
            entity.getTrialEndsAt()
        );
    }
} 