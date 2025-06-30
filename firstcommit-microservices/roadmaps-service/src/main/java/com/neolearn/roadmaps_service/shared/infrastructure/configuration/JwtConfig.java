package com.neolearn.roadmaps_service.shared.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "authorization.jwt")
@Getter
@Setter
public class JwtConfig {
    private String secret;
    private Integer expirationDays;
}