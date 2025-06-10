package com.neolearn.memberships_service.membership.domain.model.commands;

public record UpdateProfileCommand(
    Long id,
    String fullName,
    String email,
    String phone,
    String bio
) {
    public UpdateProfileCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Profile ID must be valid");
        }
        if (fullName != null && fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be blank");
        }
        if (email != null) {
            if (email.isBlank()) {
                throw new IllegalArgumentException("Email cannot be blank");
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new IllegalArgumentException("Email format is invalid");
            }
        }
    }
} 