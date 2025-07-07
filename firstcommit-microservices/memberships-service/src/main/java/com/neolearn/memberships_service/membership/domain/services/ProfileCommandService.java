package com.neolearn.memberships_service.membership.domain.services;



import com.neolearn.memberships_service.membership.domain.model.aggregates.Profile;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateProfileCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdateProfileCommand;

import java.util.Optional;

public interface ProfileCommandService {
    Optional<Profile> handle(CreateProfileCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
} 