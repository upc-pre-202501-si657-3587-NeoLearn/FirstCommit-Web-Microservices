package com.neolearn.projects_service.project.domain.services;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.domain.model.commands.*;
import com.neolearn.projects_service.project.domain.model.entities.*;

import java.util.Optional;

public interface ProjectCommandService {
    Optional<Project> handle(CreateProjectCommand command);
    Optional<Project> handle(UpdateProjectCommand command);
    Optional<Project> handle(UpdateProjectStatusCommand command);
    Optional<Invitation> handle(InviteMemberCommand command);
    Optional<Invitation> handle(RespondToInvitationCommand command);
    Optional<Resource> handle(ShareResourceCommand command);
    Optional<Message> handle(SendMessageCommand command);
    Optional<Task> handle(AssignTaskCommand command);
    Optional<Task> handle(UpdateTaskCommand command);
    Optional<Task> handle(UpdateTaskStatusCommand command);
    boolean handle(RemoveMemberCommand command);
    Optional<Requirement> handle(AddRequirementCommand command);
    Optional<ProjectTechnology> handle(AddTechnologyCommand command);
    boolean handle(RemoveTechnologyCommand command);
    Optional<User> handle(CreateUserCommand command);
} 