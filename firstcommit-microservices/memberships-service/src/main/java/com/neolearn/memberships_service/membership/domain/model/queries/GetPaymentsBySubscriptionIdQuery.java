package com.neolearn.memberships_service.membership.domain.model.queries;

public record GetPaymentsBySubscriptionIdQuery(
    Long subscriptionId
) {
    public GetPaymentsBySubscriptionIdQuery {
        if (subscriptionId == null || subscriptionId <= 0) {
            throw new IllegalArgumentException("Subscription ID must be valid");
        }
    }
} 