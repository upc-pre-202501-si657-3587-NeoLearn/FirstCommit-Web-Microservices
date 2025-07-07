package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.CreateProfileCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
            resource.getUserId(),
            resource.getFullName(),
            resource.getEmail(),
            resource.getPhone(),
            resource.getBio()
        );
    }
} 