package com.neolearn.memberships_service.membership.domain.model.commands;

public record CreateTrialSubscriptionCommand(
    Long userId,
    Long planId,
    Integer trialDays
) {
    public CreateTrialSubscriptionCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
        if (planId == null || planId <= 0) {
            throw new IllegalArgumentException("Plan ID must be valid");
        }
        if (trialDays == null || trialDays <= 0) {
            throw new IllegalArgumentException("Trial days must be greater than zero");
        }
    }
} 