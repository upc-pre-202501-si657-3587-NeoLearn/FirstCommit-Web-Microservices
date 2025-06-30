package com.neolearn.roadmaps_service.shared.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserContextService userContextService; // Tu servicio existente

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization header is missing or invalid.");
            return false;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            if (!jwtTokenUtil.validateToken(token)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "The provided JWT is invalid or has expired.");
                return false;
            }

            // El token es válido, creamos el UserContext
            Claims claims = jwtTokenUtil.extractAllClaims(token);
            UserContext userContext = new UserContext(
                    claims.get("userId", Long.class),
                    claims.getSubject(),
                    claims.get("roles", String.class), // Asumiendo que "roles" es el nombre del claim
                    claims.get("tier", String.class),    // Asumiendo que "tier" es el nombre del claim
                    claims.getExpiration()
            );

            // Establecemos el contexto para esta petición
            userContextService.setCurrentUser(userContext);
            log.info("User context set for user: {}, roles: {}", userContext.getUsuario(), userContext.getRol());

            // --- LÓGICA DE AUTORIZACIÓN MEJORADA ---
            // Ahora la lógica de autorización se basa en el UserContextService
            if (isEndpointProtected(request)) {
                if (!userContextService.hasRole("ADMIN")) { // Usamos el método de tu servicio
                    log.warn("Authorization failure. User '{}' with roles {} attempted to access ADMIN endpoint: {}",
                            userContext.getUsuario(), userContext.getRol(), request.getRequestURI());
                    response.sendError(HttpStatus.FORBIDDEN.value(), "You do not have the required ADMIN role to access this resource.");
                    return false;
                }
            }

        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage(), e);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Error processing JWT token.");
            return false;
        }

        return true;
    }

    /**
     * Define qué endpoints requieren el rol de ADMIN.
     */
    private boolean isEndpointProtected(HttpServletRequest request) {
        String method = request.getMethod();

        // Cualquier método que modifique datos (POST, PUT, DELETE, PATCH) requiere ser ADMIN.
        return !method.equalsIgnoreCase("GET");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Limpia el ThreadLocal para evitar memory leaks
        userContextService.clearCurrentUser();
    }
}