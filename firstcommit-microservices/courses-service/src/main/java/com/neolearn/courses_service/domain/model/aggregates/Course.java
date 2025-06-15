package com.neolearn.courses_service.domain.model.aggregates;

import com.neolearn.courses_service.domain.model.entities.UserProgress;
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

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProgress> userProgresses = new ArrayList<>();


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
        if (!enrolledStudents.contains(userId)) {
            this.enrolledStudents.add(userId);
        }
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

    public boolean isUserEnrolled(String userId) {
        return !this.enrolledStudents.contains(userId);
    }


}