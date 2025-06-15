package com.neolearn.courses_service.application.internal.commandservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.entities.UserContentCompletion;
import com.neolearn.courses_service.domain.model.entities.UserProgress;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.CourseRepository;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.UserContentCompletionRepository;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.UserProgressRepository;
import com.neolearn.courses_service.domain.model.commands.*;
import com.neolearn.courses_service.domain.model.valueobjects.CourseContent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CourseCommandService {

    private final CourseRepository courseRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserContentCompletionRepository userContentCompletionRepository;
    private final ObjectMapper objectMapper; // Para parsear JSON

    public CourseCommandService(CourseRepository courseRepository, UserProgressRepository userProgressRepository, UserContentCompletionRepository userContentCompletionRepository) {
        this.courseRepository = courseRepository;
        this.userProgressRepository = userProgressRepository;
        this.userContentCompletionRepository = userContentCompletionRepository;
        this.objectMapper = new ObjectMapper(); // Es thread-safe
    }

    @Transactional
    public Course handle(CreateCourseCommand command) {
        courseRepository.findByTitleAndInstructorId(command.title(), command.instructorId())
                .ifPresent(course -> {
                    throw new IllegalArgumentException("A course with this title already exists for this instructor.");
                });

        var content = new CourseContent(command.theoryContent(), command.quizContent(), command.codingContent());
        var course = new Course(command.title(), command.description(), command.category(), command.instructorId(), content);

        return courseRepository.save(course);
    }

    @Transactional
    public Optional<Course> handle(UpdateCourseCommand command) {
        return courseRepository.findById(command.courseId()).map(courseToUpdate -> {
            if (!courseToUpdate.getInstructorId().equals(command.instructorId())) {
                throw new SecurityException("Instructor " + command.instructorId() + " is not authorized to update this course.");
            }
            var newContent = new CourseContent(command.theoryContent(), command.quizContent(), command.codingContent());
            courseToUpdate.updateDetails(command.title(), command.description(), command.category(), newContent);
            return courseRepository.save(courseToUpdate);
        });
    }

    @Transactional
    public void handle(PublishCourseCommand command) {
        Course course = courseRepository.findByIdAndInstructorId(command.courseId(), command.instructorId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found or instructor not authorized."));

        course.publish();
        courseRepository.save(course);
    }

    @Transactional
    public void handle(DeleteCourseCommand command) {
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        if (!course.getInstructorId().equals(command.instructorId())) {
            throw new SecurityException("Instructor " + command.instructorId() + " is not authorized to delete this course.");
        }

        courseRepository.delete(course);
    }

    @Transactional
    public void handle(EnrollCourseCommand command) {
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        course.enrollStudent(command.userId());
        courseRepository.save(course);

        userProgressRepository.findByCourseIdAndUserId(command.courseId(), command.userId())
                .orElseGet(() -> {
                    var newProgress = new UserProgress(course, command.userId());
                    return userProgressRepository.save(newProgress);
                });
    }

    @Transactional
    public void handle(RateCourseCommand command) {
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        course.addRating(command.userId(), command.rating());
        courseRepository.save(course);
    }

    // --- NUEVO MÉTODO PARA MANEJAR EL PROGRESO GRANULAR ---
    @Transactional
    public void handle(CompleteContentCommand command) {
        // 1. Validar que el usuario esté inscrito y obtener su registro de progreso.
        UserProgress userProgress = userProgressRepository.findByCourseIdAndUserId(command.courseId(), command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User is not enrolled in this course."));

        // 2. Registrar la finalización del contenido específico.
        // Opcional: Podrías añadir lógica para evitar duplicados si el UniqueConstraint no es suficiente.
        UserContentCompletion completion = new UserContentCompletion(command.userId(), command.courseId(), command.contentId());
        userContentCompletionRepository.save(completion);

        // 3. Recalcular y actualizar el progreso general.
        updateOverallProgress(userProgress.getCourse(), command.userId());
    }

    /**
     * Recalcula el progreso general de un usuario en un curso y lo actualiza en la base de datos.
     * @param course El curso para el cual se calcula el progreso.
     * @param userId El ID del usuario.
     */
    private void updateOverallProgress(Course course, String userId) {
        int totalItems = countTotalContentItems(course);
        if (totalItems == 0) return; // No hay nada que contar, progreso es 0.

        long completedItems = userContentCompletionRepository.countByUserIdAndCourseId(userId, course.getId());

        int newProgressPercentage = (int) Math.round(((double) completedItems / totalItems) * 100);

        // Actualizamos la entidad UserProgress con el nuevo porcentaje.
        userProgressRepository.findByCourseIdAndUserId(course.getId(), userId).ifPresent(progressToUpdate -> {
            progressToUpdate.setProgress(newProgressPercentage);
            userProgressRepository.save(progressToUpdate);
        });
    }

    /**
     * Cuenta el número total de elementos de contenido "completables" en un curso.
     * @param course La entidad del curso.
     * @return El número total de lecciones, preguntas y ejercicios.
     */
    private int countTotalContentItems(Course course) {
        int total = 0;
        try {
            CourseContent content = course.getContent();

            // Contar lecciones en theoryContent
            JsonNode theory = objectMapper.readTree(content.getTheoryContent());
            if (theory != null && theory.isArray()) {
                total += theory.size();
            }

            // Contar preguntas en quizContent
            JsonNode quiz = objectMapper.readTree(content.getQuizContent());
            if (quiz != null && quiz.has("questions") && quiz.get("questions").isArray()) {
                total += quiz.get("questions").size();
            }

            // Contar ejercicios en codingContent
            JsonNode coding = objectMapper.readTree(content.getCodingContent());
            if (coding != null && coding.isArray()) {
                total += coding.size();
            }
        } catch (JsonProcessingException e) {
            // Es crucial manejar este error. Si el JSON está malformado, el cálculo fallará.
            // Aquí lo logueamos, pero en un sistema de producción podrías enviar una alerta.
            System.err.println("Error parsing course content JSON for course ID: " + course.getId() + ". " + e.getMessage());
            return 0;
        }
        return total;
    }
}