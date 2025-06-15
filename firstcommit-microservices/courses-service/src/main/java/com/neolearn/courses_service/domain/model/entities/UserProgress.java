// Ubicación: src/main/java/com/neolearn/courses_service/domain/model/aggregates/UserProgress.java
package com.neolearn.courses_service.domain.model.entities;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_progress") // Nombre de tabla explícito y limpio
@IdClass(UserProgressId.class) // Usamos la clase para la clave compuesta
@Getter
public class UserProgress {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Setter
    @Column(name = "progress")
    private int progress; // entre 0 y 100

    protected UserProgress() {}

    public UserProgress(Course course, String userId) {
        this.course = course;
        this.userId = userId;
        this.progress = 0; // Progreso inicial
    }
}