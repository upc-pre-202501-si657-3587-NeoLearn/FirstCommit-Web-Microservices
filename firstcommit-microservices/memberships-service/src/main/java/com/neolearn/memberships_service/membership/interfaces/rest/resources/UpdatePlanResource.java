package com.neolearn.memberships_service.membership.interfaces.rest.resources;



import com.neolearn.memberships_service.membership.domain.model.valueobjects.BillingPeriod;

import java.math.BigDecimal;

public class UpdatePlanResource {
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private BillingPeriod billingPeriod;
    private Integer trialDays;

    public UpdatePlanResource() {
    }

    public UpdatePlanResource(String name, String description, BigDecimal price, String currency, 
                             BillingPeriod billingPeriod, Integer trialDays) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.billingPeriod = billingPeriod;
        this.trialDays = trialDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }
} 