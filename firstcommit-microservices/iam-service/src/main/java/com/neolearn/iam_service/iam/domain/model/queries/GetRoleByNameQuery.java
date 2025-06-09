package com.neolearn.iam_service.iam.domain.model.queries ;

import com.neolearn.iam_service.iam.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles roleName) {
}