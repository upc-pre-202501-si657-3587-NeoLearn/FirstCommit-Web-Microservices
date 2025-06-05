package com.neolearn.iam_service.iam.domain.services;

import com.neolearn.iam_service.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}