package com.neolearn.memberships_service.membership.interfaces.acl;



import com.neolearn.memberships_service.membership.domain.model.aggregates.Plan;
import com.neolearn.memberships_service.membership.domain.model.aggregates.Profile;
import com.neolearn.memberships_service.membership.domain.model.aggregates.Subscription;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateProfileCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateTrialSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPlanByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByUserIdQuery;
import com.neolearn.memberships_service.membership.domain.model.valueobjects.SubscriptionStatus;
import com.neolearn.memberships_service.membership.domain.services.*;

import java.util.Optional;


public class MembershipContextFacade {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;
    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;
    private final PlanQueryService planQueryService;

    public MembershipContextFacade(
            ProfileCommandService profileCommandService,
            ProfileQueryService profileQueryService,
            SubscriptionCommandService subscriptionCommandService,
            SubscriptionQueryService subscriptionQueryService,
            PlanQueryService planQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.planQueryService = planQueryService;
    }

    /**
     * Creates a profile for a user.
     * @param userId The ID of the user.
     * @param fullName The full name of the user.
     * @param email The email of the user.
     * @return The ID of the created profile.
     */
    public Long createProfile(Long userId, String fullName, String email) {
        var command = new CreateProfileCommand(userId, fullName, email, null, null);
        var result = profileCommandService.handle(command);
        return result.map(Profile::getId).orElse(0L);
    }

    /**
     * Creates a profile for a user with additional information.
     * @param userId The ID of the user.
     * @param fullName The full name of the user.
     * @param email The email of the user.
     * @param phone The phone number of the user.
     * @param bio The bio of the user.
     * @return The ID of the created profile.
     */
    public Long createProfile(Long userId, String fullName, String email, String phone, String bio) {
        var command = new CreateProfileCommand(userId, fullName, email, phone, bio);
        var result = profileCommandService.handle(command);
        return result.map(Profile::getId).orElse(0L);
    }

    /**
     * Checks if a user has a profile.
     * @param userId The ID of the user.
     * @return True if the user has a profile, false otherwise.
     */
    public boolean hasProfile(Long userId) {
        var query = new GetProfileByUserIdQuery(userId);
        return profileQueryService.handle(query).isPresent();
    }

    /**
     * Creates a subscription for a user.
     * @param userId The ID of the user.
     * @param planId The ID of the plan.
     * @return The ID of the created subscription.
     */
    public Long createSubscription(Long userId, Long planId) {
        var command = new CreateSubscriptionCommand(userId, planId, SubscriptionStatus.ACTIVE);
        var result = subscriptionCommandService.handle(command);
        return result.map(Subscription::getId).orElse(0L);
    }

    /**
     * Creates a trial subscription for a user.
     * @param userId The ID of the user.
     * @param planId The ID of the plan.
     * @param trialDays The number of days for the trial.
     * @return The ID of the created subscription.
     */
    public Long createTrialSubscription(Long userId, Long planId, int trialDays) {
        var command = new CreateTrialSubscriptionCommand(userId, planId, trialDays);
        var result = subscriptionCommandService.handle(command);
        return result.map(Subscription::getId).orElse(0L);
    }

    /**
     * Checks if a user has an active subscription.
     * @param userId The ID of the user.
     * @return True if the user has an active subscription, false otherwise.
     */
    public boolean hasActiveSubscription(Long userId) {
        var query = new GetActiveSubscriptionByUserIdQuery(userId);
        return subscriptionQueryService.handle(query).isPresent();
    }

    /**
     * Gets the active subscription of a user.
     * @param userId The ID of the user.
     * @return The active subscription of the user, or null if the user doesn't have an active subscription.
     */
    public Optional<Subscription> getActiveSubscription(Long userId) {
        var query = new GetActiveSubscriptionByUserIdQuery(userId);
        return subscriptionQueryService.handle(query);
    }

    /**
     * Gets a plan by its ID.
     * @param planId The ID of the plan.
     * @return The plan, or null if the plan doesn't exist.
     */
    public Optional<Plan> getPlan(Long planId) {
        var query = new GetPlanByIdQuery(planId);
        return planQueryService.handle(query);
    }
} 