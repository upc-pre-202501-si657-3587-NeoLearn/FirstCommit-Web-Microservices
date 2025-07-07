package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.commands.UpdateProfileCommand;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {
    public static UpdateProfileCommand toCommandFromResource(Long profileId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(
            profileId,
            resource.getFullName(),
            null, 
            resource.getPhone(),
            resource.getBio()
        );
    }
} 