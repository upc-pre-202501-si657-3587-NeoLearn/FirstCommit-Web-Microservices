package com.neolearn.memberships_service.membership.domain.model.commands;

public record RemoveBenefitFromPlanCommand(
    Long planId,
    Long benefitId
) {
    public RemoveBenefitFromPlanCommand {
        if (planId == null || planId <= 0) {
            throw new IllegalArgumentException("Plan ID must be valid");
        }
        if (benefitId == null || benefitId <= 0) {
            throw new IllegalArgumentException("Benefit ID must be valid");
        }
    }
} 