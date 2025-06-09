package com.neolearn.projects_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "project_id", nullable = false) private Project project;
    @Column(nullable = false) private String title;
    @Lob @Column(columnDefinition = "TEXT") private String description;
    private Long assignedToUserId;
    @Column(nullable = false) private Long createdByUserId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TaskStatus status;
    private LocalDateTime dueDate;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); status = TaskStatus.TODO; }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}