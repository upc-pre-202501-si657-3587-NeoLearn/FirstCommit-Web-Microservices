package com.neolearn.iam_service.iam.domain.model.aggregates ;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.neolearn.iam_service.iam.domain.model.entities.Role;
import com.neolearn.iam_service.iam.domain.model.valueobjects.SubscriptionTier;
import com.neolearn.iam_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * User aggregate root
 * This class represents the aggregate root for the User entity.
 *
 * @see AuditableAbstractAggregateRoot
 */

@Getter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_tier", nullable = false)
    private SubscriptionTier subscriptionTier;

    public User() {
        this.roles = new HashSet<>();
        this.subscriptionTier = SubscriptionTier.getDefaultTier();
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
        this.subscriptionTier = SubscriptionTier.getDefaultTier();
    }

    public User(String username, String password, List<Role> roles) {
        this(username, password);
        addRoles(roles);
    }

    public User(String username, String password, List<Role> roles, SubscriptionTier subscriptionTier) {
        this(username, password, roles);
        this.subscriptionTier = subscriptionTier != null ? subscriptionTier : SubscriptionTier.getDefaultTier();
    }

    /**
     * Add a role to the user
     * @param role the role to add
     * @return the user with the added role
     */
    
    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    /**
     * Add a list of roles to the user
     * @param roles the list of roles to add
     * @return the user with the added roles
     */
    
    public User addRoles(List<Role> roles) {
        var validatedRoles = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoles);
        return this;
    }

    public @NotBlank @Size(max = 50) String getUsername() {
        return username;
    }

    public @NotBlank @Size(max = 120) String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public SubscriptionTier getSubscriptionTier() {
        return subscriptionTier;
    }

    public void updateSubscriptionTier(SubscriptionTier subscriptionTier) {
        this.subscriptionTier = subscriptionTier != null ? subscriptionTier : SubscriptionTier.getDefaultTier();
    }
}
