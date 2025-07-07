// Ubicación: src/main/java/com/neolearn/courses_service/domain/model/aggregates/UserProgress.java
package com.neolearn.courses_service.domain.model.entities;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Relación con el curso
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String userId;

    private int percentageCompleted;
    private String lastContentId;
    private LocalDateTime lastUpdated;

    protected UserProgress() {}

    public UserProgress(Course course, String userId) {
        this.course = course;
        this.userId = userId;
        this.percentageCompleted = 0;
        this.lastUpdated = LocalDateTime.now();
    }

    // Dentro de la clase UserProgress.java

    public void updateProgress(int newPercentage, String newLastContentId) {
        // Validación de negocio (opcional pero bueno)
        if (newPercentage < 0 || newPercentage > 100) {
            throw new IllegalArgumentException("El porcentaje de progreso debe estar entre 0 y 100.");
        }

        this.percentageCompleted = newPercentage;
        this.lastContentId = newLastContentId;
        this.lastUpdated = LocalDateTime.now(); // Suponiendo que tienes este campo
    }
}