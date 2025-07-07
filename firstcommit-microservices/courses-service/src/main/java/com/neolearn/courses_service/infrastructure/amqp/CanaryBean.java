package com.neolearn.courses_service.infrastructure.amqp;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CanaryBean {

    private static final Logger log = LoggerFactory.getLogger(CanaryBean.class);

    public CanaryBean() {
        log.info(">>>>>> ¡¡¡CanaryBean CONSTRUCTOR!!! - El escaneo de componentes está funcionando en este paquete. <<<<<<");
    }

    @PostConstruct
    public void init() {
        log.info(">>>>>> ¡¡¡CanaryBean POST-CONSTRUCT!!! - El bean está completamente vivo. <<<<<<");
    }
}
