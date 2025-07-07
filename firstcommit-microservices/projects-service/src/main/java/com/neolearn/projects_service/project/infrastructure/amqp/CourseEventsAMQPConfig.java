// EN roadmaps-service: src/main/java/com/neolearn/roadmaps_service/infrastructure/amqp/CourseEventsAMQPConfig.java
package com.neolearn.projects_service.project.infrastructure.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseEventsAMQPConfig {

    private static final String EXCHANGE_NAME = "platform.events.exchange";
    private static final String ROUTING_KEY = "course.created";
    public static final String ROADMAPS_COURSE_CREATED_QUEUE = "roadmaps.course.created.queue";

    // Es una buena práctica declarar el exchange también en el consumidor
    // para asegurar que existe. Es idempotente.
    @Bean
    public TopicExchange platformEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Crear una cola duradera para este consumidor
    @Bean
    public Queue roadmapsCourseCreatedQueue() {
        return new Queue(ROADMAPS_COURSE_CREATED_QUEUE, true);
    }

    // Crear el "binding" que une la cola al exchange con la routing key
    @Bean
    public Binding binding(Queue roadmapsCourseCreatedQueue, TopicExchange platformEventsExchange) {
        return BindingBuilder.bind(roadmapsCourseCreatedQueue)
                .to(platformEventsExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Usa el convertidor de JSON de Jackson, que es el estándar en Spring.
        return new Jackson2JsonMessageConverter();
    }
}