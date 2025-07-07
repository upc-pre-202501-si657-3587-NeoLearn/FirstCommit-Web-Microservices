package com.neolearn.projects_service.project.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
    "com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories"
})
public class JpaConfiguration {
} 