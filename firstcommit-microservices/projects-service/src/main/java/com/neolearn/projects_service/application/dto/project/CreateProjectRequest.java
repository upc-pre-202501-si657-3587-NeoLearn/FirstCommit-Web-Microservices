package com.neolearn.projects_service.application.dto.project;
import com.neolearn.projects_service.domain.model.ProjectType;
import lombok.Data;
@Data public class CreateProjectRequest {
    private String name;
    private String description;
    private ProjectType projectType;
    private String license;
}