package com.neolearn.projects_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendbirdConfig {

    @Value("${sendbird.app.id}")
    private String sendbirdAppId;

    @Value("${sendbird.api.token}")
    private String sendbirdApiToken;

    // Este archivo es principalmente un placeholder para la configuración del SDK/cliente de Sendbird.
    // El SendbirdService que te proporcioné usa @Value para inyectar las propiedades directamente.
}