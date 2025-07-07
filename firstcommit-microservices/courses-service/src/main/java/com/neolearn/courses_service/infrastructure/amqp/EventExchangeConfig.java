// src/main/java/com/neolearn/courses_service/infrastructure/amqp/EventExchangeConfig.java
package com.neolearn.courses_service.infrastructure.amqp;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventExchangeConfig {

    public static final String PLATFORM_EVENTS_EXCHANGE = "platform.events.exchange";

    @Bean
    public TopicExchange platformEventsExchange() {
        return new TopicExchange(PLATFORM_EVENTS_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Usa el convertidor de JSON de Jackson, que es el estándar en Spring.
        return new Jackson2JsonMessageConverter();
    }
}