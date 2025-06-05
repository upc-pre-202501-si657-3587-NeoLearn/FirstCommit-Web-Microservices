package com.neolearn.memberships_service.membership.interfaces.rest.resources;

public class CreateProfileResource {
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String bio;

    public CreateProfileResource() {
    }

    public CreateProfileResource(Long userId, String fullName, String email, String phone, String bio) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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