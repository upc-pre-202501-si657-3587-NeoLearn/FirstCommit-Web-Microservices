package com.neolearn.memberships_service.membership.domain.model.aggregates;

import com.neolearn.memberships_service.membership.domain.model.valueobjects.SubscriptionStatus;
import com.neolearn.memberships_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "renewal_date")
    private LocalDateTime renewalDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    public Subscription(Long userId, Long planId, SubscriptionStatus status, LocalDateTime startDate) {
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.startDate = startDate;
    }

    public static Subscription createTrialSubscription(Long userId, Long planId, int trialDays) {
        LocalDateTime now = LocalDateTime.now();
        Subscription subscription = new Subscription(userId, planId, SubscriptionStatus.TRIAL, now);
        subscription.trialEndsAt = now.plusDays(trialDays);
        return subscription;
    }

    public void activate(LocalDateTime renewalDate) {
        this.status = SubscriptionStatus.ACTIVE;
        this.renewalDate = renewalDate;
        this.trialEndsAt = null;
    }

    public void setRenewalDate(LocalDateTime renewalDate) {
        this.renewalDate = renewalDate;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELED;
        this.endDate = LocalDateTime.now();
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
        this.endDate = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }

    public boolean isInTrial() {
        return this.status == SubscriptionStatus.TRIAL && 
               this.trialEndsAt != null && 
               LocalDateTime.now().isBefore(this.trialEndsAt);
    }

    public boolean isTrialExpired() {
        return this.status == SubscriptionStatus.TRIAL && 
               this.trialEndsAt != null && 
               LocalDateTime.now().isAfter(this.trialEndsAt);
    }

    public boolean needsRenewal() {
        return (this.status == SubscriptionStatus.ACTIVE || this.status == SubscriptionStatus.TRIAL) && 
               this.renewalDate != null && 
               LocalDateTime.now().isAfter(this.renewalDate);
    }
} 