package com.neolearn.courses_service.application.internal.commandservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.entities.UserContentCompletion;
import com.neolearn.courses_service.domain.model.entities.UserProgress;
import com.neolearn.courses_service.domain.services.ICourseCommandService;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.CourseRepository;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.UserContentCompletionRepository;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.UserProgressRepository;
import com.neolearn.courses_service.domain.model.commands.*;
import com.neolearn.courses_service.domain.model.valueobjects.CourseContent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neolearn.common_events.events.CourseCreatedEvent;
import com.neolearn.courses_service.infrastructure.amqp.EventExchangeConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

@Service
public class CourseCommandService implements ICourseCommandService {

    private final CourseRepository courseRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserContentCompletionRepository userContentCompletionRepository;
    private final ObjectMapper objectMapper; // Para parsear JSON
    private final RabbitTemplate rabbitTemplate;

    public CourseCommandService(CourseRepository courseRepository, UserProgressRepository userProgressRepository, UserContentCompletionRepository userContentCompletionRepository, RabbitTemplate rabbitTemplate) {
        this.courseRepository = courseRepository;
        this.userProgressRepository = userProgressRepository;
        this.userContentCompletionRepository = userContentCompletionRepository;
        this.objectMapper = new ObjectMapper(); // Es thread-safe
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Course handle(CreateCourseCommand command) {
        // 1. Validación (opcional pero recomendada)
        if (courseRepository.findByTitleAndInstructorId(command.title(), command.instructorId()).isPresent()) {
            throw new IllegalArgumentException("Un curso con este título y autor ya existe.");
        }

        // 2. Crear el Value Object 'CourseContent'
        // Esto valida que los contenidos sean JSON válidos.
        var courseContent = new CourseContent(
                command.theoryContent(),
                command.quizContent(),
                command.codingContent()
        );

        // 3. Crear el Agregado 'Course'
        var course = new Course(
                command.title(),
                command.description(),
                command.category(),
                command.instructorId(),
                courseContent
        );

        Course savedCourse = courseRepository.saveAndFlush(course);

        // 5. Crear el evento para notificar a otros servicios
        // Usamos la entidad 'course' que ya tiene su ID.
        var event = new CourseCreatedEvent(
                savedCourse.getId().toString(),
                savedCourse.getTitle(),
                savedCourse.getDescription() // Puedes añadir más campos si los necesitas
        );

        // 6. Publicar el evento en RabbitMQ
        try {
            String routingKey = "course.created";
            rabbitTemplate.convertAndSend(EventExchangeConfig.PLATFORM_EVENTS_EXCHANGE, routingKey, event);
            // System.out.println() o log.info() para depurar
            System.out.println("Evento CourseCreatedEvent publicado para el curso ID: " + course.getId());
        } catch (Exception e) {
            // Si el envío falla, es útil registrar el error.
            // Con mandatory=true, esto lanzará una excepción que causará un rollback.
            System.err.println("¡ERROR! No se pudo publicar el evento en RabbitMQ: " + e.getMessage());
            // Relanzar la excepción para asegurar el rollback
            throw new RuntimeException("Fallo al publicar el evento de creación del curso.", e);
        }

        // 7. Devolver la entidad 'Course' completa, como lo espera el controlador.
        return savedCourse;
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
        // Primero, validamos que el curso exista y que el instructor sea el dueño
        // para evitar que un instructor borre cursos de otro.
        if (!courseRepository.existsByIdAndInstructorId(command.courseId(), command.instructorId())) {
            // Lanzas una excepción específica o una genérica.
            // Esto se traducirá en un 404 Not Found o 403 Forbidden en el frontend.
            throw new IllegalArgumentException("Course with ID " + command.courseId() + " not found or you don't have permission to delete it.");
        }

        // Si la validación pasa, borramos el curso.
        courseRepository.deleteById(command.courseId());
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
        // 1. Marcar un contenido específico como completado en la BD...
        //    userContentCompletionRepository.save(new UserContentCompletion(...));

        // 2. Cargar el Agregado Raíz
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // 3. Llamar a nuestro método privado para recalcular y actualizar el progreso
        updateOverallProgress(course, command.userId(), command.contentId());

        // 4. Guardar el Agregado Raíz. Esto persistirá los cambios en UserProgress.
        courseRepository.save(course);
    }

    /**
     * Recalcula el progreso general de un usuario en un curso y lo actualiza en la base de datos.
     *
     * @param course El curso para el cual se calcula el progreso.
     * @param userId El ID del usuario.
     */
    private void updateOverallProgress(Course course, String userId, String lastCompletedContentId) {

        // 1. Delegar el cálculo del total de ítems a la propia entidad Course.
        //    Esto encapsula la lógica y la hace reutilizable.
        int totalItems = course.getTotalContentItemsCount();

        if (totalItems == 0) {
            // Si no hay contenido, no hay nada que hacer.
            return;
        }

        // 2. Obtener la cuenta de ítems completados del repositorio.
        //    Esta es una consulta válida ya que es una lectura.
        long completedItems = userContentCompletionRepository.countByUserIdAndCourseId(userId, course.getId());

        // 3. Calcular el nuevo porcentaje de progreso.
        int newProgressPercentage = (int) Math.round(((double) completedItems / totalItems) * 100);

        // 4. Actualizar el progreso A TRAVÉS DEL AGREGADO RAÍZ (Course).
        //    Llamamos al método que ya creamos en la entidad Course.
        course.updateUserProgress(userId, newProgressPercentage, lastCompletedContentId);

        // 5. El guardado se hará en el método 'handle' público que llamó a este.
        //    NO se llama a courseRepository.save(course) aquí.
    }

    @Transactional
    public void handle(UpdateProgressCommand command) { // El tipo de retorno debe ser void, según tu interfaz

        // 1. Buscar el Agregado Raíz (Course)
        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + command.courseId()));

        // 2. Validar que el usuario esté inscrito (usando la lógica del Agregado)
        if (!course.isUserEnrolled(command.userId())) {
            throw new IllegalStateException("User with id " + command.userId() + " is not enrolled in this course.");
        }

        // 3. Delegar la operación de actualización al Agregado Raíz.
        // Creamos un nuevo método en la clase 'Course' para esto.
        course.updateUserProgress(
                command.userId(),
                command.progressPercentage(),
                command.lastContentCompletedId()
        );

        // 4. Guardar el Agregado Raíz. JPA se encargará de las entidades hijas.
        courseRepository.save(course);
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