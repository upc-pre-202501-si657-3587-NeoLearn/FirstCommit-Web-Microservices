package com.neolearn.memberships_service.membership.domain.services;

import com.neolearn.memberships_service.membership.domain.model.entities.Payment;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPaymentByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPaymentsBySubscriptionIdQuery;


import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByIdQuery query);
    List<Payment> handle(GetPaymentsBySubscriptionIdQuery query);
} 