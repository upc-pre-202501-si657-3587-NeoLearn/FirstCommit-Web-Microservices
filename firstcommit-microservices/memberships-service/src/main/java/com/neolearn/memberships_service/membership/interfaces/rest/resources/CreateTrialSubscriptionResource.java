package com.neolearn.memberships_service.membership.interfaces.rest.resources;

public class CreateTrialSubscriptionResource {
    private Long userId;
    private Long planId;
    private Integer trialDays;

    public CreateTrialSubscriptionResource() {
    }

    public CreateTrialSubscriptionResource(Long userId, Long planId, Integer trialDays) {
        this.userId = userId;
        this.planId = planId;
        this.trialDays = trialDays;
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

    public Integer getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }
} 