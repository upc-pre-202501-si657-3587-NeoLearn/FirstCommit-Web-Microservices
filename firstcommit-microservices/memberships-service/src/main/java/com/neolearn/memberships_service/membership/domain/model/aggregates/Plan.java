package com.neolearn.memberships_service.membership.domain.model.aggregates;

import com.neolearn.memberships_service.membership.domain.model.entities.Benefit;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.BillingPeriod;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.Money;
import com.neolearn.memberships_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;




import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plans")
@Getter
@NoArgsConstructor
public class Plan extends AuditableAbstractAggregateRoot<Plan> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Embedded
    private Money price;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false)
    private BillingPeriod billingPeriod;

    @Column(name = "has_trial")
    private boolean hasTrial;

    @Column(name = "trial_duration")
    private Integer trialDuration;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "plan_benefits",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "benefit_id")
    )
    private Set<Benefit> benefits;

    public Plan(String name, String description, Money price, BillingPeriod billingPeriod) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.billingPeriod = billingPeriod;
        this.hasTrial = false;
        this.benefits = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public Plan withTrial(int trialDuration) {
        this.hasTrial = true;
        this.trialDuration = trialDuration;
        return this;
    }

    public Plan addBenefit(Benefit benefit) {
        this.benefits.add(benefit);
        return this;
    }

    public Plan removeBenefit(Benefit benefit) {
        this.benefits.remove(benefit);
        return this;
    }

    public boolean includesBenefit(Long benefitId) {
        return this.benefits.stream()
                .anyMatch(benefit -> benefit.getId().equals(benefitId));
    }

    public void setPrice(Money money) {
    }
}