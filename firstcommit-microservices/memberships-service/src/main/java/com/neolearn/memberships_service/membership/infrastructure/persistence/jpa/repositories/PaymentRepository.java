package com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories;

import com.neolearn.memberships_service.membership.domain.model.entities.Payment;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.PaymentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findBySubscriptionId(Long subscriptionId);
    List<Payment> findByStatus(PaymentStatus status);
} 