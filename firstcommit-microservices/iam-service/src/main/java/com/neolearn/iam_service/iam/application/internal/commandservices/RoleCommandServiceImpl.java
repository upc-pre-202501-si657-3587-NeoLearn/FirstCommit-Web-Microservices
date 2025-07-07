package com.neolearn.iam_service.iam.application.internal.commandservices;

import com.neolearn.iam_service.iam.domain.model.commands.SeedRolesCommand;
import com.neolearn.iam_service.iam.domain.model.entities.Role;
import com.neolearn.iam_service.iam.domain.model.valueobjects.Roles;
import com.neolearn.iam_service.iam.domain.services.RoleCommandService;
import com.neolearn.iam_service.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.Arrays;

import org.springframework.stereotype.Service;

/**
 * Implementation of {@link RoleCommandService} to handle {@link SeedRolesCommand}
 */
@Service
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * This method will handle the {@link SeedRolesCommand} and will create the roles if not exists
     * @param command {@link SeedRolesCommand}
     * @see SeedRolesCommand
     */
    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if(!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }
}