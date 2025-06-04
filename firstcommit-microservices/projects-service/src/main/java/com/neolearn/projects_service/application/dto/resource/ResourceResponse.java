package com.neolearn.projects_service.application.dto.resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor public class ResourceResponse {
    private Long id; private Long projectId; private String name; private String type;
    private String location; private Long uploadedByUserId; private LocalDateTime createdAt;
}