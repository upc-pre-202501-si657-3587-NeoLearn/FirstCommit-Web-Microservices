package com.neolearn.memberships_service.membership.domain.model.commands;



import com.neolearn.memberships_service.membership.domain.model.valueobjects.BillingPeriod;

import java.math.BigDecimal;
import java.util.List;

public record CreatePlanCommand(
    String name,
    String description,
    BigDecimal price,
    String currencyCode,
    BillingPeriod billingPeriod,
    boolean hasTrial,
    Integer trialDuration,
    List<Long> benefitIds
) {
    public CreatePlanCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new IllegalArgumentException("Currency code cannot be null or blank");
        }
        if (billingPeriod == null) {
            throw new IllegalArgumentException("Billing period cannot be null");
        }
        if (hasTrial && (trialDuration == null || trialDuration <= 0)) {
            throw new IllegalArgumentException("Trial duration must be greater than zero when trial is enabled");
        }
    }
} 