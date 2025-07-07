package com.neolearn.iam_service.iam.domain.model.commands;

import java.util.List;

import com.neolearn.iam_service.iam.domain.model.entities.Role;

public record SignUpCommand(String username, String password, List<Role> roles) {
}