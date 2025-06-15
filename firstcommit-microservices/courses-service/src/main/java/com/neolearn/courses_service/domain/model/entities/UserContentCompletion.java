package com.neolearn.courses_service.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_content_completion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "content_id"})) // Un usuario solo puede completar un contenido una vez
@EntityListeners(AuditingEntityListener.class)
@Getter
public class UserContentCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "content_id", nullable = false)
    private String contentId; // "lec_001", "q_001", "ex_001" de tu JSON

    @CreatedDate
    @Column(name = "completed_at", nullable = false, updatable = false)
    private LocalDateTime completedAt;

    protected UserContentCompletion() {}

    public UserContentCompletion(String userId, Long courseId, String contentId) {
        this.userId = userId;
        this.courseId = courseId;
        this.contentId = contentId;
    }
}
