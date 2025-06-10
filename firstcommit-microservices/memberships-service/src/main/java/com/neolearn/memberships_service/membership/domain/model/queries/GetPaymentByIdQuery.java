package com.neolearn.memberships_service.membership.domain.model.queries;

public record GetPaymentByIdQuery(
    Long id
) {
    public GetPaymentByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Payment ID must be valid");
        }
    }
} 