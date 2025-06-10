package com.neolearn.memberships_service.membership.domain.model.commands;



import com.neolearn.memberships_service.membership.domain.model.valueobjects.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePaymentCommand(
    Long subscriptionId,
    BigDecimal amount,
    String currencyCode,
    LocalDateTime date,
    PaymentStatus status,
    String paymentMethod
) {
    public CreatePaymentCommand {
        if (subscriptionId == null || subscriptionId <= 0) {
            throw new IllegalArgumentException("Subscription ID must be valid");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new IllegalArgumentException("Currency code cannot be null or blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method cannot be null or blank");
        }
    }
} 