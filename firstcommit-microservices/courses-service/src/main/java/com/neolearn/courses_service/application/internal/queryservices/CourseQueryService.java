package com.neolearn.courses_service.application.internal.queryservices;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.queries.*;
import com.neolearn.courses_service.domain.services.ICourseQueryService;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.CourseRepository;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.UserProgressRepository;
import com.neolearn.courses_service.interfaces.rest.resources.CourseContentResource;
import com.neolearn.courses_service.interfaces.rest.resources.CourseDetailsResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // readOnly = true es una optimización para consultas
public class CourseQueryService implements ICourseQueryService {

    private final CourseRepository courseRepository;
    private final UserProgressRepository userProgressRepository; // Inyectar el nuevo repositorio

    public CourseQueryService(CourseRepository courseRepository, UserProgressRepository userProgressRepository) {
        this.courseRepository = courseRepository;
        this.userProgressRepository = userProgressRepository;
    }

    @Override
    public List<Course> handle(GetAllCoursesQuery query) {
        if (query.onlyPublished()) {
            return courseRepository.findByPublishedTrue();
        }
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> handle(GetCourseByIdQuery query) {
        // La validación en el record de la Query es preferible, pero aquí está bien también.
        if (query.courseId() == null || query.courseId() <= 0) {
            throw new IllegalArgumentException("Invalid course ID");
        }
        return courseRepository.findById(query.courseId());
    }

    @Override
    public List<Course> handle(GetEnrolledCoursesQuery query) {
        if (query.userId() == null || query.userId().isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return courseRepository.findCoursesByEnrolledUser(query.userId());
    }

    @Override
    public CourseDetailsResource handle(GetCourseDetailsQuery query) {
        if (query.courseId() == null || query.courseId() <= 0) {
            throw new IllegalArgumentException("Invalid course ID");
        }

        // 1. Obtener el curso. Si no existe, lanzar excepción.
        Course course = courseRepository.findById(query.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + query.courseId()));

        // 2. Obtener el progreso del usuario (si se proporcionó un userId)
        Integer progress = null;
        if (query.userId() != null && !query.userId().isBlank()) {
            // Usamos el nuevo repositorio para obtener el progreso.
            progress = userProgressRepository.findByCourseIdAndUserId(query.courseId(), query.userId())
                    .map(userProgress -> userProgress.getProgress()) // Extraemos el valor int del progreso
                    .orElse(null); // Si no hay registro, el progreso es nulo
        }

        // 3. Ensamblar el recurso de detalles
        return new CourseDetailsResource(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getInstructorId(),
                course.getImageUrl(),
                course.isPublished(),
                course.getAverageRating(),
                progress, // Usamos la variable de progreso obtenida
                new CourseContentResource(
                        course.getContent().getTheoryContent(),
                        course.getContent().getQuizContent(),
                        course.getContent().getCodingContent()
                )
        );
    }

    @Override
    public Optional<Integer> handle(GetUserProgressQuery query) {
        if (query.courseId() == null || query.courseId() <= 0) {
            throw new IllegalArgumentException("Invalid course ID");
        }
        if (query.userId() == null || query.userId().isBlank()) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        // CORRECCIÓN: Usamos el UserProgressRepository directamente. Es mucho más eficiente.
        return userProgressRepository.findByCourseIdAndUserId(query.courseId(), query.userId())
                .map(userProgress -> userProgress.getProgress()); // Mapeamos el Optional<UserProgress> a Optional<Integer>
    }
}