package com.neolearn.memberships_service.membership.application.internal.queryservices;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Profile;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByEmailQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByUserIdQuery;
import com.neolearn.memberships_service.membership.domain.services.ProfileQueryService;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.id());
    }

    @Override
    public Optional<Profile> handle(GetProfileByUserIdQuery query) {
        return profileRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<Profile> handle(GetProfileByEmailQuery query) {
        return profileRepository.findByEmail(query.email());
    }
} 