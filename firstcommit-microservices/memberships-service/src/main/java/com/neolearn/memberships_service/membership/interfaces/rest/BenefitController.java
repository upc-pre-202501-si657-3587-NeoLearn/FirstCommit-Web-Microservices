package com.neolearn.memberships_service.membership.interfaces.rest;


import com.neolearn.memberships_service.membership.domain.model.commands.CreateBenefitCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdateBenefitCommand;
import com.neolearn.memberships_service.membership.domain.model.queries.GetAllBenefitsQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetBenefitByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetBenefitsByTypeQuery;
import com.neolearn.memberships_service.membership.domain.services.BenefitCommandService;
import com.neolearn.memberships_service.membership.domain.services.BenefitQueryService;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.BenefitResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateBenefitResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.UpdateBenefitResource;
import com.neolearn.memberships_service.membership.interfaces.rest.transform.BenefitResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value= "/api/v1/benefits", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Benefits", description = "Benefits Management Endpoints")
public class BenefitController {

    private final BenefitQueryService benefitQueryService;
    private final BenefitCommandService benefitCommandService;

    public BenefitController(BenefitQueryService benefitQueryService, BenefitCommandService benefitCommandService) {
        this.benefitQueryService = benefitQueryService;
        this.benefitCommandService = benefitCommandService;
    }

    @PostMapping
    public ResponseEntity<BenefitResource> createBenefit(@RequestBody CreateBenefitResource resource){
        var createBenefitCommand = new CreateBenefitCommand(
            resource.getName(),
            resource.getDescription(),
            resource.getType()
        );
        var benefit = benefitCommandService.handle(createBenefitCommand);
        if(benefit.isEmpty()) return ResponseEntity.badRequest().build();
        var benefitResource = BenefitResourceFromEntityAssembler.toResourceFromEntity(benefit.get());
        return new ResponseEntity<>(benefitResource, HttpStatus.CREATED);
    }

    @PutMapping("/{benefitId}")
    public ResponseEntity<BenefitResource> updateBenefit(@PathVariable Long benefitId, @RequestBody UpdateBenefitResource resource){
        var updateBenefitCommand = new UpdateBenefitCommand(
            benefitId,
            resource.getName(),
            resource.getDescription(),
            resource.getType()
        );
        var benefit = benefitCommandService.handle(updateBenefitCommand);
        if(benefit.isEmpty()) return ResponseEntity.notFound().build();
        var benefitResource = BenefitResourceFromEntityAssembler.toResourceFromEntity(benefit.get());
        return ResponseEntity.ok(benefitResource);
    }

    @GetMapping("/{benefitId}")
    public ResponseEntity<BenefitResource> getBenefitById(@PathVariable Long benefitId){
        var getBenefitByIdQuery = new GetBenefitByIdQuery(benefitId);
        var benefit = benefitQueryService.handle(getBenefitByIdQuery);
        if(benefit.isEmpty()) return ResponseEntity.notFound().build();
        var benefitResource = BenefitResourceFromEntityAssembler.toResourceFromEntity(benefit.get());
        return ResponseEntity.ok(benefitResource);
    }

    @GetMapping
    public ResponseEntity<List<BenefitResource>> getAllBenefits() {
        var benefits = benefitQueryService.handle(new GetAllBenefitsQuery());
        if(benefits.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        var benefitResources = benefits.stream().map(BenefitResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(benefitResources);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<BenefitResource>> getBenefitsByType(@PathVariable String type) {
        var getBenefitsByTypeQuery = new GetBenefitsByTypeQuery(type);
        var benefits = benefitQueryService.handle(getBenefitsByTypeQuery);
        if(benefits.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        var benefitResources = benefits.stream().map(BenefitResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(benefitResources);
    }
} 