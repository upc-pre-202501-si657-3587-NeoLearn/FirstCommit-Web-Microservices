package com.neolearn.memberships_service.membership.domain.services;



import com.neolearn.memberships_service.membership.domain.model.aggregates.Subscription;
import com.neolearn.memberships_service.membership.domain.model.commands.CancelSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateTrialSubscriptionCommand;

import java.util.Optional;

public interface SubscriptionCommandService {
    Optional<Subscription> handle(CreateSubscriptionCommand command);
    Optional<Subscription> handle(CreateTrialSubscriptionCommand command);
    Optional<Subscription> handle(CancelSubscriptionCommand command);
} 