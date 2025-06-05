package com.neolearn.memberships_service.membership.application.internal.commandservices;

import com.neolearn.memberships_service.membership.domain.model.commands.CreateBenefitCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdateBenefitCommand;
import com.neolearn.memberships_service.membership.domain.model.entities.Benefit;
import com.neolearn.memberships_service.membership.domain.services.BenefitCommandService;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.BenefitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BenefitCommandServiceImpl implements BenefitCommandService {

    private static final String BENEFIT_NOT_FOUND = "Benefit not found with id: ";
    private static final String BENEFIT_EXISTS = "Benefit with name '%s' already exists";
    
    private final BenefitRepository benefitRepository;

    public BenefitCommandServiceImpl(BenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    @Override
    public Optional<Benefit> handle(CreateBenefitCommand command) {
        validateBenefitDoesNotExist(command.name());
        
        Benefit benefit = new Benefit(
            command.name().trim(),
            command.description().trim(),
            command.type()
        );
        
        return Optional.of(benefitRepository.save(benefit));
    }

    @Override
    public Optional<Benefit> handle(UpdateBenefitCommand command) {
        return Optional.ofNullable(benefitRepository.findById(command.id())
                .map(benefit -> {

                    if (!benefit.getName().equals(command.name())) {
                        validateBenefitDoesNotExist(command.name());
                    }

                    benefit.updateDetails(
                            command.name().trim(),
                            command.description().trim(),
                            command.type()
                    );

                    return benefitRepository.save(benefit);
                })
                .orElseThrow(() -> new IllegalArgumentException(BENEFIT_NOT_FOUND + command.id())));
    }
    
    private void validateBenefitDoesNotExist(String name) {
        if (benefitRepository.existsByName(name.trim())) {
            throw new IllegalStateException(String.format(BENEFIT_EXISTS, name));
        }
    }
}
