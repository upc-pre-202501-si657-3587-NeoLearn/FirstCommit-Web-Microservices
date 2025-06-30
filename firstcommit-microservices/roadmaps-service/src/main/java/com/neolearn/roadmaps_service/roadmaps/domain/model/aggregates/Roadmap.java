package com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates;

import com.neolearn.roadmaps_service.roadmaps.domain.model.entities.CourseReference;
import com.neolearn.roadmaps_service.roadmaps.domain.model.exceptions.CourseAlreadyInRoadmapException;
import com.neolearn.roadmaps_service.roadmaps.domain.model.exceptions.CourseNotFoundInRoadmapException;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.CourseId;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Roadmap {

    private final RoadmapId id;
    private String name;
    private String description;
    private final List<CourseReference> courses;

    // Constructor para un nuevo Roadmap
    public Roadmap(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Roadmap name cannot be blank");
        }
        this.id = RoadmapId.create();
        this.name = name;
        this.description = description;
        this.courses = new ArrayList<>();
    }

    // Constructor para reconstruir desde persistencia
    public Roadmap(RoadmapId id, String name, String description, List<CourseReference> courses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courses = new ArrayList<>(courses); // Copia defensiva
    }

    // --- Métodos de Negocio (Comportamiento) ---

    public void addCourse(CourseReference newCourse) {
        if (courses.stream().anyMatch(c -> c.courseId().equals(newCourse.courseId()))) {
            throw new CourseAlreadyInRoadmapException(newCourse.courseId());
        }
        this.courses.add(newCourse);
    }

    public void removeCourse(CourseId courseIdToRemove) {
        boolean removed = this.courses.removeIf(c -> c.courseId().equals(courseIdToRemove));
        if (!removed) {
            throw new CourseNotFoundInRoadmapException(courseIdToRemove);
        }
    }

    public void updateDetails(String newName, String newDescription) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Roadmap name cannot be blank");
        }
        this.name = newName;
        this.description = newDescription;
    }


    // --- Getters (Protegiendo el estado interno) ---

    public RoadmapId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // ¡Importante! Devolvemos una copia inmutable para no exponer la lista interna.
    public List<CourseReference> getCourses() {
        return Collections.unmodifiableList(courses);
    }
}