package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.entities.Payment;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {

    public static PaymentResource toResourceFromEntity(Payment entity) {
        return new PaymentResource(
            entity.getId(),
            entity.getSubscriptionId(),
            entity.getAmount().getAmount(),
            entity.getAmount().getCurrencyCode(),
            entity.getPaymentMethod(),
            entity.getStatus(),
            entity.getDate()
        );
    }
} 