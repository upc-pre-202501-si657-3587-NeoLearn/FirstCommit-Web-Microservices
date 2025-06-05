package com.neolearn.memberships_service.membership.interfaces.rest.resources;


import com.neolearn.memberships_service.membership.domain.model.valueobjects.SubscriptionStatus;

import java.time.LocalDateTime;

public class SubscriptionResource {
    private Long id;
    private Long userId;
    private Long planId;
    private SubscriptionStatus status;
    private boolean active;
    private boolean inTrial;
    private LocalDateTime startDate;
    private LocalDateTime renewalDate;
    private LocalDateTime endDate;
    private LocalDateTime trialEndsAt;

    public SubscriptionResource() {
    }

    public SubscriptionResource(Long id, Long userId, Long planId, SubscriptionStatus status, boolean active, boolean inTrial, LocalDateTime startDate, LocalDateTime renewalDate, LocalDateTime endDate, LocalDateTime trialEndsAt) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.active = active;
        this.inTrial = inTrial;
        this.startDate = startDate;
        this.renewalDate = renewalDate;
        this.endDate = endDate;
        this.trialEndsAt = trialEndsAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isInTrial() {
        return inTrial;
    }

    public void setInTrial(boolean inTrial) {
        this.inTrial = inTrial;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDateTime renewalDate) {
        this.renewalDate = renewalDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getTrialEndsAt() {
        return trialEndsAt;
    }

    public void setTrialEndsAt(LocalDateTime trialEndsAt) {
        this.trialEndsAt = trialEndsAt;
    }
} 