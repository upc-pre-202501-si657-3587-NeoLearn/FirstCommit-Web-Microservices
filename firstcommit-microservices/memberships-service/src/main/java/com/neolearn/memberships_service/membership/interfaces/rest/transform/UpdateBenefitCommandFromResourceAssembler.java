package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.UpdateBenefitCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.UpdateBenefitResource;

public class UpdateBenefitCommandFromResourceAssembler {
    public static UpdateBenefitCommand toCommandFromResource(Long benefitId, UpdateBenefitResource resource) {
        return new UpdateBenefitCommand(
            benefitId,
            resource.getName(),
            resource.getDescription(),
            resource.getType()
        );
    }
} 