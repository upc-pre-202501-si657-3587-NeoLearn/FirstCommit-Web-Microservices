package com.neolearn.projects_service.application.dto.project;
import com.neolearn.projects_service.domain.model.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor public class ProjectResponse {
    private Long id; private String name; private String description;
    private ProjectType projectType; private Long ownerUserId; private String license;
    private String sendbirdChannelId; private LocalDateTime createdAt; private LocalDateTime updatedAt;
}