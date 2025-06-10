package com.neolearn.memberships_service.membership.domain.services;



import com.neolearn.memberships_service.membership.domain.model.commands.CreateBenefitCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdateBenefitCommand;
import com.neolearn.memberships_service.membership.domain.model.entities.Benefit;

import java.util.Optional;

public interface BenefitCommandService {
    Optional<Benefit> handle(CreateBenefitCommand command);
    Optional<Benefit> handle(UpdateBenefitCommand command);
} 