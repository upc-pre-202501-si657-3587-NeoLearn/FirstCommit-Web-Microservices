package com.neolearn.roadmaps_service.roadmaps.infrastructure.amqp;

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

    // --- NOMBRES CONSTANTES ---
    private static final String EXCHANGE_NAME = "platform.events.exchange";
    private static final String ROUTING_KEY_COURSE_CREATED = "course.created";
    public static final String QUEUE_NAME_COURSE_CREATED = "roadmaps.course.created.queue";

    // --- BEAN DEL EXCHANGE (Buena práctica declararlo también aquí) ---
    @Bean
    public TopicExchange platformEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // --- BEAN DE LA COLA (Esto ya funciona, porque la vemos en la UI) ---
    @Bean
    public Queue roadmapsCourseCreatedQueue() {
        return new Queue(QUEUE_NAME_COURSE_CREATED, true); // durable = true
    }

    // --- BEAN DEL BINDING (¡¡¡ESTE ES EL SOSPECHOSO!!!) ---
    // Este método une la cola al exchange con la routing key.
    @Bean
    public Binding bindingCourseCreated(Queue roadmapsCourseCreatedQueue, TopicExchange platformEventsExchange) {
        return BindingBuilder
                .bind(roadmapsCourseCreatedQueue)
                .to(platformEventsExchange)
                .with(ROUTING_KEY_COURSE_CREATED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Usa el convertidor de JSON de Jackson, que es el estándar en Spring.
        return new Jackson2JsonMessageConverter();
    }
}