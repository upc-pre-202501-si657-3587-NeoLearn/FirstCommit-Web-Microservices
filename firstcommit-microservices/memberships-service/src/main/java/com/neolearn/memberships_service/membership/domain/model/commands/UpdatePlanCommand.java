package com.neolearn.memberships_service.membership.domain.model.commands;



import com.neolearn.memberships_service.membership.domain.model.valueobjects.BillingPeriod;

import java.math.BigDecimal;

public record UpdatePlanCommand(
    Long id,
    String name,
    String description,
    BigDecimal price,
    String currencyCode,
    BillingPeriod billingPeriod,
    boolean hasTrial,
    Integer trialDuration
) {
    public UpdatePlanCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Plan ID must be valid");
        }
        if (name != null && name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (description != null && description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (currencyCode != null && currencyCode.isBlank()) {
            throw new IllegalArgumentException("Currency code cannot be blank");
        }
        if (hasTrial && (trialDuration == null || trialDuration <= 0)) {
            throw new IllegalArgumentException("Trial duration must be greater than zero when trial is enabled");
        }
    }
} 