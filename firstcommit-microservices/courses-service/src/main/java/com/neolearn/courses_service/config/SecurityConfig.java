package com.neolearn.courses_service.config; // Asegúrate que el paquete sea el correcto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Es buena práctica mantenerlo, aunque no se use @PreAuthorize ahora
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Paso 1: Deshabilitar la protección CSRF (común para APIs stateless)
                .csrf(csrf -> csrf.disable())

                // Paso 2: Configurar las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitir todas las peticiones a cualquier URL sin autenticación
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}