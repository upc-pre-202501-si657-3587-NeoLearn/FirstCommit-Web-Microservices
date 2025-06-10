package com.neolearn.memberships_service.membership.interfaces.rest.transform;


import com.neolearn.memberships_service.membership.domain.model.entities.Benefit;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.BenefitResource;

public class BenefitResourceFromEntityAssembler {

    public static BenefitResource toResourceFromEntity(Benefit entity) {
        return new BenefitResource(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType()
        );
    }
} 