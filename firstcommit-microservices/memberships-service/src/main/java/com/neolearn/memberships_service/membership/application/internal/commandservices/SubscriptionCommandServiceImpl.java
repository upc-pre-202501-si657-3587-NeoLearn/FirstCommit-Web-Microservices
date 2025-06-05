package com.neolearn.memberships_service.membership.application.internal.commandservices;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Plan;
import com.neolearn.memberships_service.membership.domain.model.aggregates.Subscription;
import com.neolearn.memberships_service.membership.domain.model.commands.CancelSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateTrialSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.services.SubscriptionCommandService;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.PlanRepository;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository, PlanRepository planRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    @Override
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        // Check if user already has an active subscription
        if (subscriptionRepository.existsActiveSubscriptionForUser(command.userId())) {
            throw new IllegalStateException("User already has an active subscription");
        }
        
        // Check if plan exists
        Optional<Plan> optionalPlan = planRepository.findById(command.planId());
        if (optionalPlan.isEmpty()) {
            throw new IllegalArgumentException("Plan not found");
        }
        
        // Create subscription
        Subscription subscription = new Subscription(command.userId(), command.planId(), command.status(), LocalDateTime.now());
        
        // Set renewal date based on billing period
        Plan plan = optionalPlan.get();
        LocalDateTime renewalDate = LocalDateTime.now();
        switch (plan.getBillingPeriod()) {
            case MONTHLY:
                renewalDate = renewalDate.plusMonths(1);
                break;
            case ANNUAL:
                renewalDate = renewalDate.plusYears(1);
                break;
        }
        
        subscription.setRenewalDate(renewalDate);
        return Optional.of(subscriptionRepository.save(subscription));
    }

    @Override
    public Optional<Subscription> handle(CreateTrialSubscriptionCommand command) {
        // Check if user already has an active subscription
        if (subscriptionRepository.existsActiveSubscriptionForUser(command.userId())) {
            throw new IllegalStateException("User already has an active subscription");
        }
        
        // Check if plan exists and has trial
        Optional<Plan> optionalPlan = planRepository.findById(command.planId());
        if (optionalPlan.isEmpty()) {
            throw new IllegalArgumentException("Plan not found");
        }
        
        Plan plan = optionalPlan.get();
        if (!plan.isHasTrial()) {
            throw new IllegalStateException("Plan does not offer a trial period");
        }
        
        // Create trial subscription
        Subscription subscription = Subscription.createTrialSubscription(command.userId(), command.planId(), command.trialDays());
        
        // Set renewal date based on trial duration
        LocalDateTime renewalDate = LocalDateTime.now().plusDays(command.trialDays());
        subscription.setRenewalDate(renewalDate);
        
        return Optional.of(subscriptionRepository.save(subscription));
    }

    @Override
    public Optional<Subscription> handle(CancelSubscriptionCommand command) {
        return subscriptionRepository.findById(command.subscriptionId())
                .map(subscription -> {
                    subscription.cancel();
                    return subscriptionRepository.save(subscription);
                });
    }
} 