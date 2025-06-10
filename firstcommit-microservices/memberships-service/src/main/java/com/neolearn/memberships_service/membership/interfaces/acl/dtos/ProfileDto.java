package com.neolearn.memberships_service.membership.interfaces.acl.dtos;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Profile;

public class ProfileDto {
    private final Long id;
    private final Long userId;
    private final String fullName;
    private final String email;
    private final String phone;

    public ProfileDto(Profile profile) {
        this.id = profile.getId();
        this.userId = profile.getUserId();
        this.fullName = profile.getFullName();
        this.email = profile.getEmail();
        this.phone = profile.getPhone();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
} 