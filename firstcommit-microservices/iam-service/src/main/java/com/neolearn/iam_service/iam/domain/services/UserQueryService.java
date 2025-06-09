package com.neolearn.iam_service.iam.domain.services;

import java.util.List;
import java.util.Optional;

import com.neolearn.iam_service.iam.domain.model.aggregates.User;
import com.neolearn.iam_service.iam.domain.model.queries.GetAllUsersQuery;
import com.neolearn.iam_service.iam.domain.model.queries.GetUserByIdQuery;
import com.neolearn.iam_service.iam.domain.model.queries.GetUserByUsernameQuery;

public interface UserQueryService {
    List<User> handle(GetAllUsersQuery query);
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByUsernameQuery query);
}