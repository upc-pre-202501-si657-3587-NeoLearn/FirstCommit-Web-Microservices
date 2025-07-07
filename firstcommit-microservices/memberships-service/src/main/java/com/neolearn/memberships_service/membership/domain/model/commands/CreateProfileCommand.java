package com.neolearn.memberships_service.membership.domain.model.commands;

public record CreateProfileCommand(
    Long userId,
    String fullName,
    String email,
    String phone,
    String bio
) {
    public CreateProfileCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
} 