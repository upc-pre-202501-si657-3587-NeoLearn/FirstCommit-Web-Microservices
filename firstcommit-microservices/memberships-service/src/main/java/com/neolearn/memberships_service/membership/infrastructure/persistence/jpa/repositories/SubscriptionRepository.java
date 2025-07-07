package com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Subscription;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    
    List<Subscription> findByStatus(SubscriptionStatus status);
    
    @Query("SELECT s FROM Subscription s WHERE s.userId = :userId AND s.status = 'ACTIVE'")
    Optional<Subscription> findActiveByUserId(@Param("userId") Long userId);
    
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Subscription s WHERE s.userId = :userId AND s.status = 'ACTIVE'")
    boolean existsActiveSubscriptionForUser(@Param("userId") Long userId);
} 