package com.neolearn.roadmaps_service.roadmaps.infrastructure.amqp;

import com.neolearn.common_events.events.CourseCreatedEvent;
// Importamos el nuevo comando y el servicio
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.RegisterAvailableCourseCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CourseEventsListener {

    private static final Logger log = LoggerFactory.getLogger(CourseEventsListener.class);

    // Inyectamos la INTERFAZ del servicio, no la implementación (mejor práctica)
    private final RoadmapCommandService roadmapCommandService;

    public CourseEventsListener(RoadmapCommandService roadmapCommandService) {
        this.roadmapCommandService = roadmapCommandService;
    }

    @RabbitListener(queues = CourseEventsAMQPConfig.QUEUE_NAME_COURSE_CREATED)
    public void onCourseCreated(CourseCreatedEvent event) {
        log.info("Received CourseCreatedEvent for courseId: {}", event.courseId());

        try {
            // 1. Traducir el evento a un comando del dominio
            var command = new RegisterAvailableCourseCommand(event.courseId(), event.title());

            // 2. Despachar el comando usando el servicio
            roadmapCommandService.handle(command);

            log.info("Successfully dispatched RegisterAvailableCourseCommand for courseId: {}", event.courseId());
        } catch (Exception e) {
            log.error("Failed to process CourseCreatedEvent for courseId: {}. Error: {}", event.courseId(), e.getMessage(), e);
            throw e; // Relanzar para que RabbitMQ sepa del fallo
        }
    }
}