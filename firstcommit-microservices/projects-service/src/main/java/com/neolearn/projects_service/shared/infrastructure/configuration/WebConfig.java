package com.neolearn.projects_service.shared.infrastructure.configuration;

import com.neolearn.projects_service.shared.infrastructure.jwt.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**") // Aplica a todos los endpoints de la API
                .excludePathPatterns(
                        "/api/v1/health",           // Excluir health checks
                        "/swagger-ui/**",           // Excluir Swagger UI
                        "/v3/api-docs/**",          // Excluir OpenAPI docs
                        "/actuator/**"              // Excluir endpoints de Actuator
                );
    }
}