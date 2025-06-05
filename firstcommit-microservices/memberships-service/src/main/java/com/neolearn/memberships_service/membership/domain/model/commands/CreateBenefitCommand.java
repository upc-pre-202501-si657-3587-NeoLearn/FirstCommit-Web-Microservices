package com.neolearn.memberships_service.membership.domain.model.commands;


import com.neolearn.memberships_service.membership.domain.model.valueobjects.BenefitType;

public record CreateBenefitCommand(
    String name,
    String description,
    BenefitType type
) {
    public CreateBenefitCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Benefit type cannot be null");
        }
    }
} 