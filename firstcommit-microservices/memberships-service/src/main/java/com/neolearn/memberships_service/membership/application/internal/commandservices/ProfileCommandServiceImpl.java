package com.neolearn.memberships_service.membership.application.internal.commandservices;


import com.neolearn.memberships_service.membership.domain.model.aggregates.Profile;
import com.neolearn.memberships_service.membership.domain.model.commands.CreateProfileCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdateProfileCommand;
import com.neolearn.memberships_service.membership.domain.services.ProfileCommandService;
import com.neolearn.memberships_service.membership.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(CreateProfileCommand command) {
        // Check if a profile already exists for this user
        if (profileRepository.existsByUserId(command.userId())) {
            throw new IllegalStateException("A profile already exists for this user");
        }
        
        // Check if email is already in use
        if (profileRepository.existsByEmail(command.email())) {
            throw new IllegalStateException("Email is already in use");
        }
        
        // Create profile
        Profile profile = new Profile(command.userId(), command.fullName(), command.email());
        
        // Set optional fields if provided
        if (command.phone() != null) {
            profile.updatePhone(command.phone());
        }
        
        if (command.bio() != null) {
            profile.updateBio(command.bio());
        }
        
        return Optional.of(profileRepository.save(profile));
    }

    @Override
    public Optional<Profile> handle(UpdateProfileCommand command) {
        return profileRepository.findById(command.id())
                .map(profile -> {
                    // Update fullName if provided
                    if (command.fullName() != null) {
                        profile.updateFullName(command.fullName());
                    }
                    
                    // Update email if provided and not already in use by another profile
                    if (command.email() != null) {
                        profileRepository.findByEmail(command.email())
                                .ifPresent(existingProfile -> {
                                    if (!existingProfile.getId().equals(command.id())) {
                                        throw new IllegalStateException("Email is already in use");
                                    }
                                });
                        
                        profile.updateEmail(command.email());
                    }
                    
                    // Update phone if provided
                    if (command.phone() != null) {
                        profile.updatePhone(command.phone());
                    }
                    
                    // Update bio if provided
                    if (command.bio() != null) {
                        profile.updateBio(command.bio());
                    }
                    
                    return profileRepository.save(profile);
                });
    }
} 