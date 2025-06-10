package com.neolearn.memberships_service.membership.domain.model.commands;


import com.neolearn.memberships_service.membership.domain.model.valueobjects.SubscriptionStatus;

public record CreateSubscriptionCommand(
    Long userId,
    Long planId,
    SubscriptionStatus status
) {
    public CreateSubscriptionCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
        if (planId == null || planId <= 0) {
            throw new IllegalArgumentException("Plan ID must be valid");
        }
        if (status == null) {
            throw new IllegalArgumentException("Subscription status cannot be null");
        }
    }
} 