package com.neolearn.memberships_service.membership.domain.services;



import com.neolearn.memberships_service.membership.domain.model.aggregates.Subscription;
import com.neolearn.memberships_service.membership.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetSubscriptionByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetSubscriptionsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> handle(GetSubscriptionByIdQuery query);
    List<Subscription> handle(GetSubscriptionsByUserIdQuery query);
    Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query);
} 