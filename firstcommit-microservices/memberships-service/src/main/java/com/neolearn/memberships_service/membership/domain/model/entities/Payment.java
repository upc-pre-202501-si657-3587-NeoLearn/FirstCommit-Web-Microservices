package com.neolearn.memberships_service.membership.domain.model.entities;

import com.neolearn.memberships_service.membership.domain.model.valueobjects.Money;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.PaymentStatus;
import com.neolearn.memberships_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;

/**
 * Entity that represents a payment for a subscription
 */
@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @Embedded
    private Money amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    public Payment(Long subscriptionId, Money amount, LocalDateTime date, PaymentStatus status, String paymentMethod) {
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }


    public void markAsCompleted() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
    }

    public void markAsRefunded() {
        this.status = PaymentStatus.REFUNDED;
    }
} 