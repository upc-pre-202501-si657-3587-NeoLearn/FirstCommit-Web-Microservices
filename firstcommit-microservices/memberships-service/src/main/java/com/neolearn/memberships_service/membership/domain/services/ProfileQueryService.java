package com.neolearn.memberships_service.membership.domain.services;



import com.neolearn.memberships_service.membership.domain.model.aggregates.Profile;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByEmailQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByUserIdQuery;

import java.util.Optional;

public interface ProfileQueryService {
    Optional<Profile> handle(GetProfileByIdQuery query);
    Optional<Profile> handle(GetProfileByUserIdQuery query);
    Optional<Profile> handle(GetProfileByEmailQuery query);
} 