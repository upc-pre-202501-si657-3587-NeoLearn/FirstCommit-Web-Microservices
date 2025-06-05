package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.CreateBenefitCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateBenefitResource;

public class CreateBenefitCommandFromResourceAssembler {
    public static CreateBenefitCommand toCommandFromResource(CreateBenefitResource resource) {
        return new CreateBenefitCommand(
            resource.getName(),
            resource.getDescription(),
            resource.getType()
        );
    }
} 