package com.neolearn.memberships_service.membership.interfaces.rest.resources;



import com.neolearn.memberships_service.membership.domain.model.valueobjects.BillingPeriod;

import java.math.BigDecimal;
import java.util.List;

public class PlanResource {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String currencyCode;
    private BillingPeriod billingPeriod;
    private Integer trialDays;
    private List<BenefitResource> benefits;

    public PlanResource() {
    }

    public PlanResource(Long id, String name, String description, BigDecimal price, String currencyCode, 
                        BillingPeriod billingPeriod, Integer trialDays, List<BenefitResource> benefits) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currencyCode = currencyCode;
        this.billingPeriod = billingPeriod;
        this.trialDays = trialDays;
        this.benefits = benefits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public List<BenefitResource> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<BenefitResource> benefits) {
        this.benefits = benefits;
    }
} 