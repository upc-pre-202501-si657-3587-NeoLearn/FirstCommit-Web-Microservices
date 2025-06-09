package com.neolearn.projects_service.application.dto.project;
import lombok.Data;
@Data public class UpdateProjectRequest {
    private String name; private String description; private String license;
}