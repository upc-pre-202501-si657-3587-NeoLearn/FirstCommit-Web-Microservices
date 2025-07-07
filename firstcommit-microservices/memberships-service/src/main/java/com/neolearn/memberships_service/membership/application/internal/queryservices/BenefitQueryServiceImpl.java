package com.neolearn.memberships_service.membership.application.internal.queryservices;


import com.neolearn.memberships_service.membership.domain.model.entities.Benefit;
import com.neolearn.memberships_service.membership.domain.model.queries.GetAllBenefitsQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetBenefitByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetBenefitsByTypeQuery;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.BenefitType;
import com.neolearn.memberships_service.membership.domain.services.BenefitQueryService;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.BenefitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BenefitQueryServiceImpl implements BenefitQueryService {
    private final BenefitRepository benefitRepository;

    public BenefitQueryServiceImpl(BenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    @Override
    public Optional<Benefit> handle(GetBenefitByIdQuery query) {
        return benefitRepository.findById(query.id());
    }

    @Override
    public List<Benefit> handle(GetAllBenefitsQuery query) {
        return benefitRepository.findAll();
    }

    @Override
    public List<Benefit> handle(GetBenefitsByTypeQuery query) {
        BenefitType benefitType = BenefitType.valueOf(query.type());
        return benefitRepository.findByType(benefitType);
    }
} 