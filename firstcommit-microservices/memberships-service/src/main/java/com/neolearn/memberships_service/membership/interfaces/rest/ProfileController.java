package com.neolearn.memberships_service.membership.interfaces.rest;


import com.neolearn.memberships_service.membership.domain.model.commands.CreateProfileCommand;
import com.neolearn.memberships_service.membership.domain.model.commands.UpdateProfileCommand;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByEmailQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByIdQuery;
import com.neolearn.memberships_service.membership.domain.model.queries.GetProfileByUserIdQuery;
import com.neolearn.memberships_service.membership.domain.services.ProfileCommandService;
import com.neolearn.memberships_service.membership.domain.services.ProfileQueryService;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.CreateProfileResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.ProfileResource;
import com.neolearn.memberships_service.membership.interfaces.rest.resources.UpdateProfileResource;
import com.neolearn.memberships_service.membership.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value= "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Profiles", description = "Profile Management Endpoints")
public class ProfileController {

    private final ProfileQueryService profileQueryService;
    private final ProfileCommandService profileCommandService;

    public ProfileController(ProfileQueryService profileQueryService, ProfileCommandService profileCommandService) {
        this.profileQueryService = profileQueryService;
        this.profileCommandService = profileCommandService;
    }

    @PostMapping
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileResource resource){
        var createProfileCommand = new CreateProfileCommand(
            resource.getUserId(),
            resource.getFullName(),
            resource.getEmail(),
            resource.getPhone(),
            resource.getBio()
        );
        var profile = profileCommandService.handle(createProfileCommand);
        if(profile.isEmpty()) return ResponseEntity.badRequest().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(profileResource, HttpStatus.CREATED);
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ProfileResource> updateProfile(@PathVariable Long profileId, @RequestBody UpdateProfileResource resource){
        var updateProfileCommand = new UpdateProfileCommand(
            profileId,
            resource.getFullName(),
            null,
            resource.getPhone(),
            resource.getBio()
        );
        var profile = profileCommandService.handle(updateProfileCommand);
        if(profile.isEmpty()) return ResponseEntity.notFound().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResource> getProfileById(@PathVariable Long profileId){
        var getProfileByIdQuery = new GetProfileByIdQuery(profileId);
        var profile = profileQueryService.handle(getProfileByIdQuery);
        if(profile.isEmpty()) return ResponseEntity.notFound().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ProfileResource> getProfileByUserId(@PathVariable Long userId) {
        var getProfileByUserIdQuery = new GetProfileByUserIdQuery(userId);
        var profile = profileQueryService.handle(getProfileByUserIdQuery);
        if(profile.isEmpty()) return ResponseEntity.notFound().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ProfileResource> getProfileByEmail(@PathVariable String email) {
        var getProfileByEmailQuery = new GetProfileByEmailQuery(email);
        var profile = profileQueryService.handle(getProfileByEmailQuery);
        if(profile.isEmpty()) return ResponseEntity.notFound().build();
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }
} 