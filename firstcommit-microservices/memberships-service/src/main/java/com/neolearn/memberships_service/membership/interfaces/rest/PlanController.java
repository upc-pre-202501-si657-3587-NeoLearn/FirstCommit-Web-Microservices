package com.neolearn.memberships_service.membership.interfaces.rest;


import com.neolearn.memberships_service.membership.domain.model.commands.AddBenefitToPlanCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.RemoveBenefitFromPlanCommand;
import com.neolearn.memberships_service.membership.domain.model.queries.GetAllPlansQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetPlanByIdQuery;
import com.neolearn.memberships_service.membership.domain.services.PlanCommandService;
import com.neolearn.memberships_service.membership.domain.services.PlanQueryService;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreatePlanResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.PlanResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.UpdatePlanResource;
import com.neolearn.memberships_service.membership.interfaces.rest.transform.CreatePlanCommandFromResourceAssembler;
import com.neolearn.memberships_service.membership.interfaces.rest.transform.PlanResourceFromEntityAssembler;
import com.neolearn.memberships_service.membership.interfaces.rest.transform.UpdatePlanCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value= "/api/v1/plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Plans", description = "Plans Management Endpoints")
public class PlanController {

    private final PlanQueryService planQueryService;
    private final PlanCommandService planCommandService;

    public PlanController(PlanQueryService planQueryService, PlanCommandService planCommandService) {
        this.planQueryService = planQueryService;
        this.planCommandService = planCommandService;
    }

    @PostMapping
    public ResponseEntity<PlanResource> createPlan(@RequestBody CreatePlanResource resource){
        var createPlanCommand = CreatePlanCommandFromResourceAssembler.toCommandFromResource(resource);
        var plan = planCommandService.handle(createPlanCommand);
        if(plan.isEmpty()) return ResponseEntity.badRequest().build();
        var planResource = PlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());
        return new ResponseEntity<>(planResource, HttpStatus.CREATED);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<PlanResource> updatePlan(@PathVariable Long planId, @RequestBody UpdatePlanResource resource){
        var updatePlanCommand = UpdatePlanCommandFromResourceAssembler.toCommandFromResource(planId, resource);
        var plan = planCommandService.handle(updatePlanCommand);
        if(plan.isEmpty()) return ResponseEntity.notFound().build();
        var planResource = PlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());
        return ResponseEntity.ok(planResource);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<PlanResource> getPlanById(@PathVariable Long planId){
        var getPlanByIdQuery = new GetPlanByIdQuery(planId);
        var plan = planQueryService.handle(getPlanByIdQuery);
        if(plan.isEmpty()) return ResponseEntity.notFound().build();
        var planResource = PlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());
        return ResponseEntity.ok(planResource);
    }

    @GetMapping
    public ResponseEntity<List<PlanResource>> getAllPlans() {
        var plans = planQueryService.handle(new GetAllPlansQuery());
        if(plans.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        var planResources = plans.stream().map(PlanResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(planResources);
    }

    @PostMapping("/{planId}/benefits/{benefitId}")
    public ResponseEntity<PlanResource> addBenefitToPlan(@PathVariable Long planId, @PathVariable Long benefitId) {
        var command = new AddBenefitToPlanCommand(planId, benefitId);
        var plan = planCommandService.handle(command);
        if(plan.isEmpty()) return ResponseEntity.badRequest().build();
        var planResource = PlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());
        return ResponseEntity.ok(planResource);
    }

    @DeleteMapping("/{planId}/benefits/{benefitId}")
    public ResponseEntity<PlanResource> removeBenefitFromPlan(@PathVariable Long planId, @PathVariable Long benefitId) {
        var command = new RemoveBenefitFromPlanCommand(planId, benefitId);
        var plan = planCommandService.handle(command);
        if(plan.isEmpty()) return ResponseEntity.badRequest().build();
        var planResource = PlanResourceFromEntityAssembler.toResourceFromEntity(plan.get());
        return ResponseEntity.ok(planResource);
    }
} 