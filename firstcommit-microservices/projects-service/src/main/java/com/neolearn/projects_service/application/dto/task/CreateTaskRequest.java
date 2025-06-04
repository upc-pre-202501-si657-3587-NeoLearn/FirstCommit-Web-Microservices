package com.neolearn.projects_service.application.dto.task;
import lombok.Data; import java.time.LocalDateTime;
@Data public class CreateTaskRequest {
    private String title; private String description;
    private Long assignedToUserId; private LocalDateTime dueDate;
}