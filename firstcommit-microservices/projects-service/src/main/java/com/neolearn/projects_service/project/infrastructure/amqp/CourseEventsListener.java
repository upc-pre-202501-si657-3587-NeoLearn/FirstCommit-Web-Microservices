// EN roadmaps-service: src/main/java/com/neolearn/roadmaps_service/infrastructure/amqp/CourseEventsListener.java
package com.neolearn.projects_service.project.infrastructure.amqp;

import com.neolearn.common_events.events.CourseCreatedEvent; // <-- Importar desde common-events
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CourseEventsListener {

    private static final Logger log = LoggerFactory.getLogger(CourseEventsListener.class);

    // Aquí podrías inyectar un UseCase, ej. private final AddCourseToCatalogUseCase useCase;

    @RabbitListener(queues = CourseEventsAMQPConfig.ROADMAPS_COURSE_CREATED_QUEUE)
    public void onCourseCreated(CourseCreatedEvent event) {
        log.info("Received CourseCreatedEvent: {}", event);

        // --- LÓGICA DE NEGOCIO ---
        // Aquí llamarías a tu capa de aplicación/dominio.
        // Por ejemplo:
        // 1. Crear una entidad "AvailableCourse" en la BD de roadmaps.
        // 2. Invalidar una caché que contenga la lista de cursos.
        // 3. Simplemente registrar el evento por ahora.
        // useCase.execute(event.courseId(), event.title());
        log.info("Processing event for course '{}' with ID '{}'", event.title(), event.courseId());
    }
}