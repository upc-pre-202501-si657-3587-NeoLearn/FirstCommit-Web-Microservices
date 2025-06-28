package com.neolearn.projects_service.project.domain.services;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.domain.model.entities.*;
import com.neolearn.projects_service.project.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ProjectQueryService {
    Optional<Project> handle(GetProjectByIdQuery query);
    List<Project> handle(GetPlatformProjectsQuery query);
    List<Project> handle(GetUserProjectsQuery query);
    List<ProjectMember> handle(GetProjectMembersQuery query);
    List<Invitation> handle(GetProjectInvitationsQuery query);
    List<Invitation> handle(GetUserInvitationsQuery query);
    List<Resource> handle(GetProjectResourcesQuery query);
    List<Message> handle(GetProjectMessagesQuery query);
    List<Task> handle(GetProjectTasksQuery query);
    List<Task> handle(GetUserTasksQuery query);
    List<Requirement> handle(GetProjectRequirementsQuery query);
    List<Technology> handle(GetProjectTechnologiesQuery query);
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByEmailQuery query);
    Optional<User> handle(GetUserByUsernameQuery query);
} 