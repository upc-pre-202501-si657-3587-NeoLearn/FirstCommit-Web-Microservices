package com.neolearn.iam_service.iam.interfaces.rest.transform;

import com.neolearn.iam_service.iam.domain.model.commands.SignInCommand;
import com.neolearn.iam_service.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.username(), signInResource.password());
    }
}