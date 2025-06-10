package com.neolearn.memberships_service.membership.interfaces.rest.resources;



import com.neolearn.memberships_service.membership.domain.model.valueobjects.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResource {
    private Long id;
    private Long subscriptionId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    public PaymentResource() {
    }

    public PaymentResource(Long id, Long subscriptionId, BigDecimal amount, String currency, String paymentMethod, PaymentStatus status, LocalDateTime paymentDate) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
} 