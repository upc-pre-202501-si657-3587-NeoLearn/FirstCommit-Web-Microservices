package com.neolearn.courses_service.application.internal.commandservices;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.commands.*;
import com.neolearn.courses_service.domain.model.valueobjects.CourseContent;
import com.neolearn.courses_service.domain.services.ICourseCommandService;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CourseCommandService implements ICourseCommandService {

    private final CourseRepository courseRepository;

    public CourseCommandService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public Course handle(CreateCourseCommand command) {
        courseRepository.findByTitleAndInstructorId(command.title(), command.instructorId())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("El instructor ya tiene un curso con este título");
                });

        Course course = new Course(
                command.title(),
                command.description(),
                command.category(),
                command.instructorId(),
                new CourseContent(command.theoryContent(), command.quizContent(), command.codingContent())
        );

        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Optional<Course> handle(UpdateCourseCommand command) {
        return courseRepository.findByIdAndInstructorId(command.courseId(), command.instructorId())
                .map(course -> {
                    course.updateDetails(
                            command.title(),
                            command.description(),
                            command.category(),
                            new CourseContent(command.theoryContent(), command.quizContent(), command.codingContent())
                    );
                    return courseRepository.save(course);
                });
    }

    @Override
    @Transactional
    public void handle(PublishCourseCommand command) {
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        if (!course.getInstructorId().equals(command.instructorId())) {
            throw new SecurityException("Solo el instructor puede publicar el curso");
        }

        course.publish();
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void handle(EnrollCourseCommand command) {
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        if (!course.isPublished()) {
            throw new IllegalStateException("No puedes inscribirte en un curso no publicado");
        }

        course.enrollStudent(command.userId());
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void handle(RateCourseCommand command) {
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        if (!course.isUserEnrolled(command.userId())) {  // Corregida la condición
            throw new IllegalStateException("Debes estar inscrito para calificar");
        }

        course.addRating(command.userId(), command.rating());
        courseRepository.save(course);
    }

    @Override  // Ahora es parte de la interfaz
    @Transactional
    public void handle(UpdateProgressCommand command) {
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        if (!course.isUserEnrolled(command.userId())) {  // Corregida la condición
            throw new IllegalStateException("Usuario no inscrito");
        }

        course.updateUserProgress(command.userId(), command.progress());
        courseRepository.save(course);
    }
}