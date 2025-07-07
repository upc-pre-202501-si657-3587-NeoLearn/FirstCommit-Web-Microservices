package com.neolearn.memberships_service.membership.domain.services;

import com.neolearn.memberships_service.membership.domain.model.commands.CreatePaymentCommand;
import com.neolearn.memberships_service.membership.domain.model.entities.Payment;


import java.util.Optional;

public interface PaymentCommandService {
    Optional<Payment> handle(CreatePaymentCommand command);
} 