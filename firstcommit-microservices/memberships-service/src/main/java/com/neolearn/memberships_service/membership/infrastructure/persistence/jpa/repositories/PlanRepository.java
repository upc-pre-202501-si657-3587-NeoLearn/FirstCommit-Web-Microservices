package com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByName(String name);

    boolean existsByName(String name);
} 