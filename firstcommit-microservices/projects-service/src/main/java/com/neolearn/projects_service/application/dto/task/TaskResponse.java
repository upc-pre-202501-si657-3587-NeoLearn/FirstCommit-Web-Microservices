package com.neolearn.projects_service.application.dto.task;
import com.neolearn.projects_service.domain.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor public class TaskResponse {
    private Long id; private Long projectId; private String title; private String description;
    private Long assignedToUserId; private Long createdByUserId; private TaskStatus status;
    private LocalDateTime dueDate; private LocalDateTime createdAt; private LocalDateTime updatedAt;
}