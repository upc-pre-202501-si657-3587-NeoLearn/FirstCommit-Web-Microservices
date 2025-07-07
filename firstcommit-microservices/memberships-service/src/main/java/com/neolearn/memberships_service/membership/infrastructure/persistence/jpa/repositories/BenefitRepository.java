package com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories;


import com.neolearn.memberships_service.membership.domain.model.entities.Benefit;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.BenefitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    Optional<Benefit> findByName(String name);
    List<Benefit> findByType(BenefitType type);

    boolean existsByName(String trim);
} 