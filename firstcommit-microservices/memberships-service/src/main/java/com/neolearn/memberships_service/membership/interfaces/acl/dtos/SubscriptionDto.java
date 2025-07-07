package com.neolearn.memberships_service.membership.interfaces.acl.dtos;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Subscription;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.SubscriptionStatus;

import java.time.LocalDateTime;

public class SubscriptionDto {
    private final Long id;
    private final Long userId;
    private final Long planId;
    private final String planName;
    private final SubscriptionStatus status;
    private final boolean isActive;
    private final boolean isInTrial;
    private final LocalDateTime startDate;
    private final LocalDateTime renewalDate;
    private final LocalDateTime endDate;
    private final LocalDateTime trialEndsAt;

    public SubscriptionDto(Subscription subscription, String planName) {
        this.id = subscription.getId();
        this.userId = subscription.getUserId();
        this.planId = subscription.getPlanId();
        this.planName = planName;
        this.status = subscription.getStatus();
        this.isActive = subscription.isActive();
        this.isInTrial = subscription.isInTrial();
        this.startDate = subscription.getStartDate();
        this.renewalDate = subscription.getRenewalDate();
        this.endDate = subscription.getEndDate();
        this.trialEndsAt = subscription.getTrialEndsAt();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPlanId() {
        return planId;
    }

    public String getPlanName() {
        return planName;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isInTrial() {
        return isInTrial;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getRenewalDate() {
        return renewalDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getTrialEndsAt() {
        return trialEndsAt;
    }
} 