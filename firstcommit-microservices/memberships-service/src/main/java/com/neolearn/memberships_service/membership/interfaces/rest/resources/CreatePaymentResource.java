package com.neolearn.memberships_service.membership.interfaces.rest.resources;



import com.neolearn.memberships_service.membership.domain.model.valueobjects.PaymentStatus;

import java.math.BigDecimal;

public class CreatePaymentResource {
    private Long subscriptionId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private PaymentStatus paymentStatus;

    public CreatePaymentResource() {
    }

    public CreatePaymentResource(Long subscriptionId, BigDecimal amount, String currency, String paymentMethod, PaymentStatus paymentStatus) {
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
} 