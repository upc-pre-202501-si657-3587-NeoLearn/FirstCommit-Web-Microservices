package com.neolearn.projects_service.application.dto.task;
import com.neolearn.projects_service.domain.model.TaskStatus;
import lombok.Data;
import java.time.LocalDateTime;
@Data public class UpdateTaskRequest {
    private String title; private String description; private Long assignedToUserId;
    private TaskStatus status; private LocalDateTime dueDate;
}