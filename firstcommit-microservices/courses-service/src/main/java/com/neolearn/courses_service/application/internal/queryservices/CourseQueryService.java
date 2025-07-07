package com.neolearn.courses_service.application.internal.queryservices;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.entities.UserProgress;
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

    private Integer findUserProgressPercentage(Course course, String userId) {
        if (userId == null || userId.isBlank()) {
            return null; // O 0, dependiendo de lo que prefiera la UI. 'null' es más explícito.
        }

        // OPTIMIZACIÓN: Buscamos en la colección que ya ha sido cargada con el curso
        // si la relación es EAGER, o si la sesión de Hibernate está abierta.
        return course.getUserProgresses().stream()
                .filter(progress -> progress.getUserId().equals(userId))
                .findFirst()
                .map(UserProgress::getPercentageCompleted)
                .orElse(0); // Devuelve 0 si el usuario está inscrito pero no ha empezado.
    }

    @Override
    public CourseDetailsResource handle(GetCourseDetailsQuery query) {
        // La validación de la query puede vivir en su propio constructor de record
        // if (query.courseId() == null || query.courseId() <= 0) ...

        // 1. Obtener el curso. Si no existe, lanzar una excepción clara.
        //    Esto evita tener que manejar 'Optional' en el resto del método.
        Course course = courseRepository.findById(query.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + query.courseId()));

        // 2. Obtener el progreso del usuario de forma más limpia.
        //    La lógica para obtener el progreso se encapsula en un método privado.
        Integer userProgressPercentage = findUserProgressPercentage(course, query.userId());

        // 3. Ensamblar el DTO. El código se vuelve más legible.
        return new CourseDetailsResource(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getInstructorId(),
                course.getImageUrl(),
                course.isPublished(),
                course.getAverageRating(),
                userProgressPercentage, // Progreso obtenido
                new CourseContentResource( // Ensamblaje del contenido
                        course.getContent().getTheoryContent(),
                        course.getContent().getQuizContent(),
                        course.getContent().getCodingContent()
                )
        );
    }

    @Override
    public Optional<Integer> handle(GetUserProgressQuery query) {
        // Esta implementación ya es eficiente y correcta.
        return userProgressRepository.findByCourseIdAndUserId(query.courseId(), query.userId())
                .map(UserProgress::getPercentageCompleted); // Asumiendo que el método getter se llama así
    }
}