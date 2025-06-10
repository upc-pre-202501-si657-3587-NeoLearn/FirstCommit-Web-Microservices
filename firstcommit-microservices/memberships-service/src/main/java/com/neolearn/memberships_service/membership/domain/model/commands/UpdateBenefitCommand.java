package com.neolearn.memberships_service.membership.domain.model.commands;


import com.neolearn.memberships_service.membership.domain.model.valueobjects.BenefitType;

public record UpdateBenefitCommand(
    Long id,
    String name,
    String description,
    BenefitType type
) {
    public UpdateBenefitCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Benefit ID must be valid");
        }
        if (name != null && name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (description != null && description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
    }
} 