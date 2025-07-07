package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Profile;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(Profile entity) {
        return new ProfileResource(
            entity.getId(),
            entity.getUserId(),
            entity.getFullName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getBio()
        );
    }
} 