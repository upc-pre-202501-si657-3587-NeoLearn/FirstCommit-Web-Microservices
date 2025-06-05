package com.neolearn.memberships_service.membership.interfaces.rest.resources;

public class UpdateProfileResource {
    private String fullName;
    private String phone;
    private String bio;

    public UpdateProfileResource() {
    }

    public UpdateProfileResource(String fullName, String phone, String bio) {
        this.fullName = fullName;
        this.phone = phone;
        this.bio = bio;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
} 