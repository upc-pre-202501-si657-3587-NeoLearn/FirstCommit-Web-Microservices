package com.neolearn.memberships_service.membership.interfaces.rest;


import com.neolearn.memberships_service.membership.domain.model.commands.CancelSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateTrialSubscriptionCommand;
import com.neolearn.memberships_service.membership.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetSubscriptionByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetSubscriptionsByUserIdQuery;
import com.neolearn.memberships_service.membership.domain.services.SubscriptionCommandService;
import com.neolearn.memberships_service.membership.domain.services.SubscriptionQueryService;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateSubscriptionResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateTrialSubscriptionResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.SubscriptionResource;
import com.neolearn.memberships_service.membership.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value= "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Subscriptions", description = "Subscription Management Endpoints")
public class SubscriptionController {

    private final SubscriptionQueryService subscriptionQueryService;
    private final SubscriptionCommandService subscriptionCommandService;

    public SubscriptionController(SubscriptionQueryService subscriptionQueryService, SubscriptionCommandService subscriptionCommandService) {
        this.subscriptionQueryService = subscriptionQueryService;
        this.subscriptionCommandService = subscriptionCommandService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResource> createSubscription(@RequestBody CreateSubscriptionResource resource){
        var createSubscriptionCommand = new CreateSubscriptionCommand(
            resource.getUserId(),
            resource.getPlanId(),
            resource.getStatus()
        );
        var subscription = subscriptionCommandService.handle(createSubscriptionCommand);
        if(subscription.isEmpty()) return ResponseEntity.badRequest().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return new ResponseEntity<>(subscriptionResource, HttpStatus.CREATED);
    }

    @PostMapping("/trial")
    public ResponseEntity<SubscriptionResource> createTrialSubscription(@RequestBody CreateTrialSubscriptionResource resource){
        var createTrialSubscriptionCommand = new CreateTrialSubscriptionCommand(
            resource.getUserId(),
            resource.getPlanId(),
            resource.getTrialDays()
        );
        var subscription = subscriptionCommandService.handle(createTrialSubscriptionCommand);
        if(subscription.isEmpty()) return ResponseEntity.badRequest().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return new ResponseEntity<>(subscriptionResource, HttpStatus.CREATED);
    }

    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResource> cancelSubscription(@PathVariable Long subscriptionId){
        var cancelSubscriptionCommand = new CancelSubscriptionCommand(subscriptionId);
        var subscription = subscriptionCommandService.handle(cancelSubscriptionCommand);
        if(subscription.isEmpty()) return ResponseEntity.notFound().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return ResponseEntity.ok(subscriptionResource);
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResource> getSubscriptionById(@PathVariable Long subscriptionId){
        var getSubscriptionByIdQuery = new GetSubscriptionByIdQuery(subscriptionId);
        var subscription = subscriptionQueryService.handle(getSubscriptionByIdQuery);
        if(subscription.isEmpty()) return ResponseEntity.notFound().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return ResponseEntity.ok(subscriptionResource);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResource>> getSubscriptionsByUserId(@PathVariable Long userId) {
        var getSubscriptionsByUserIdQuery = new GetSubscriptionsByUserIdQuery(userId);
        var subscriptions = subscriptionQueryService.handle(getSubscriptionsByUserIdQuery);
        if(subscriptions.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        var subscriptionResources = subscriptions.stream().map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(subscriptionResources);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<SubscriptionResource> getActiveSubscriptionByUserId(@PathVariable Long userId) {
        var getActiveSubscriptionByUserIdQuery = new GetActiveSubscriptionByUserIdQuery(userId);
        var subscription = subscriptionQueryService.handle(getActiveSubscriptionByUserIdQuery);
        if(subscription.isEmpty()) return ResponseEntity.notFound().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return ResponseEntity.ok(subscriptionResource);
    }
} 