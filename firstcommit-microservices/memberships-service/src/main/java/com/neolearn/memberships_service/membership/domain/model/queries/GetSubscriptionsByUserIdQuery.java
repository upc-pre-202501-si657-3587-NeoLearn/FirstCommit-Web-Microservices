package com.neolearn.memberships_service.membership.domain.model.queries;

public record GetSubscriptionsByUserIdQuery(
    Long userId
) {
    public GetSubscriptionsByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
    }
} 