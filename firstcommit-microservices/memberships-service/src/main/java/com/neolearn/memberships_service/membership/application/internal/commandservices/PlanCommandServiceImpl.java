package com.neolearn.memberships_service.membership.application.internal.commandservices;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Plan;
import com.neolearn.memberships_service.membership.domain.model.commands.AddBenefitToPlanCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreatePlanCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.RemoveBenefitFromPlanCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdatePlanCommand;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.Money;
import com.neolearn.memberships_service.membership.domain.services.PlanCommandService;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.BenefitRepository;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.PlanRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanCommandServiceImpl implements PlanCommandService {

    private final PlanRepository planRepository;
    private final BenefitRepository benefitRepository;

    public PlanCommandServiceImpl(PlanRepository planRepository, BenefitRepository benefitRepository) {
        this.planRepository = planRepository;
        this.benefitRepository = benefitRepository;
    }

    @Override
    public Optional<Plan> handle(CreatePlanCommand command) {
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();


        boolean hasRequiredRole = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!hasRequiredRole) {
            throw new SecurityException("Only admins can create a plan");
        }

        if (planRepository.existsByName(command.name())) {
            throw new IllegalStateException("Plan with name " + command.name() + " already exists");
        }
        */
        // Crear el plan
        Money price = new Money(command.price(), command.currencyCode());
        Plan plan = new Plan(command.name(), command.description(), price, command.billingPeriod());
        
        if (command.hasTrial()) {
            plan.withTrial(command.trialDuration());
        }
        
        // Add benefits if provided
        if (command.benefitIds() != null && !command.benefitIds().isEmpty()) {
            for (Long benefitId : command.benefitIds()) {
                benefitRepository.findById(benefitId).ifPresent(plan::addBenefit);
            }
        }
        
        return Optional.of(planRepository.save(plan));
    }

    @Override
    public Optional<Plan> handle(UpdatePlanCommand command) {
        return planRepository.findById(command.id())
            .map(plan -> {
                if (command.name() != null) plan.setName(command.name());
                if (command.description() != null) plan.setDescription(command.description());
                if (command.price() != null && command.currencyCode() != null) {
                    plan.setPrice(new Money(command.price(), command.currencyCode()));
                }
                if (command.billingPeriod() != null) {
                    plan.setBillingPeriod(command.billingPeriod());
                }
                // Update trial settings
                if (command.hasTrial() && command.trialDuration() != null) {
                    plan.withTrial(command.trialDuration());
                }
                return planRepository.save(plan);
            });
    }

    @Override
    public Optional<Plan> handle(AddBenefitToPlanCommand command) {
        return planRepository.findById(command.planId())
            .flatMap(plan -> benefitRepository.findById(command.benefitId())
                .map(benefit -> {
                    plan.addBenefit(benefit);
                    return planRepository.save(plan);
                }));
    }

    @Override
    public Optional<Plan> handle(RemoveBenefitFromPlanCommand command) {
        return planRepository.findById(command.planId())
            .flatMap(plan -> benefitRepository.findById(command.benefitId())
                .map(benefit -> {
                    plan.removeBenefit(benefit);
                    return planRepository.save(plan);
                }));
    }
}