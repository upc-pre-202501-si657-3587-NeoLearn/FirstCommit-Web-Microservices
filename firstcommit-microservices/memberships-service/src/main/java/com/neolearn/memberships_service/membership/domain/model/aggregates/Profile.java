package com.neolearn.memberships_service.membership.domain.model.aggregates;

import com.neolearn.memberships_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "profiles")
@Getter
@NoArgsConstructor
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column(length = 1000)
    private String bio;

    public Profile(Long userId, String fullName, String email) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
    }

    public Profile updateFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Profile updateEmail(String email) {
        this.email = email;
        return this;
    }

    public Profile updatePhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Profile updateBio(String bio) {
        this.bio = bio;
        return this;
    }
} 