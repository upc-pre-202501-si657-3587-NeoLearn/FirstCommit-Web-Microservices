package com.neolearn.memberships_service.membership.domain.model.queries;

public record GetActiveSubscriptionByUserIdQuery(
    Long userId
) {
    public GetActiveSubscriptionByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
    }
} 