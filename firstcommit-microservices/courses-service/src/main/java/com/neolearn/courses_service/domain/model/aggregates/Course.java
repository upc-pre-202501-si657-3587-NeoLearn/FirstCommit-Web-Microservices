package com.neolearn.courses_service.domain.model.aggregates;

import com.neolearn.courses_service.domain.model.events.CoursePublishedEvent;
import com.neolearn.courses_service.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.*;

@Getter
@Entity
public class Course extends AbstractAggregateRoot<Course> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String category;
    private String imageUrl;

    @Embedded
    private CourseContent content;

    private String instructorId;
    private boolean published = false;

    @ElementCollection
    private Map<String, Integer> ratings = new HashMap<>();// userId -> rating (1-5)

    @ElementCollection
    private List<String> enrolledStudents = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "courses_user_progress",  // ¡Este nombre debe coincidir con tu consulta SQL!
            joinColumns = @JoinColumn(name = "course_id")
    )
    @MapKeyColumn(name = "user_id")  // Columna para la clave del mapa (user_id)
    @Column(name = "progress")       // Columna para el valor (progress)
    private Map<String, Integer> userProgress = new HashMap<>();

    protected Course() {}

    public Course(String title, String description, String category, String instructorId, CourseContent content) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.instructorId = instructorId;
        this.content = content;
    }

    public void publish() {
        if (this.published) {
            throw new IllegalStateException("El curso ya está publicado");
        }
        this.published = true;
        registerEvent(new CoursePublishedEvent(this.id));
    }

    public void updateDetails(String title, String description, String category, CourseContent content) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.content = content;
    }

    public void enrollStudent(String userId) {
        if (!this.published) {
            throw new IllegalStateException("No se puede inscribir a un curso no publicado");
        }
        this.enrolledStudents.add(userId);
        this.userProgress.put(userId, 0); // Progreso inicial 0%
    }

    public void updateUserProgress(String userId, int progress) {
        if (!this.enrolledStudents.contains(userId)) {
            throw new IllegalArgumentException("El usuario no está inscrito");
        }
        this.userProgress.put(userId, Math.min(100, Math.max(0, progress)));
    }

    public void addRating(String userId, int rating) {
        if (!this.enrolledStudents.contains(userId)) {
            throw new IllegalStateException("El usuario debe estar inscrito para calificar");
        }
        this.ratings.put(userId, rating);
    }

    public double getAverageRating() {
        return ratings.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public Optional<Integer> getUserProgress(String userId) {
        return Optional.ofNullable(this.userProgress.get(userId));
    }

    public boolean isUserEnrolled(String userId) {
        return this.enrolledStudents.contains(userId);
    }


}