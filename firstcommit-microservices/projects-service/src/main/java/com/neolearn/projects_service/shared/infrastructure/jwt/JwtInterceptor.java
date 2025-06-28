package com.neolearn.projects_service.shared.infrastructure.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserContextService userContextService;

    public JwtInterceptor(JwtTokenUtil jwtTokenUtil, UserContextService userContextService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userContextService = userContextService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                if (jwtTokenUtil.validateToken(token)) {
                    // Extract user information from JWT
                    Long userId = jwtTokenUtil.extractUserId(token);
                    String username = jwtTokenUtil.extractUsername(token);
                    String role = jwtTokenUtil.extractRole(token);
                    String tierSubscription = jwtTokenUtil.extractTierSubscription(token);

                    // Log the decoded information
                    log.info("=== JWT TOKEN DECODING ===");
                    log.info("Username: {}", username);
                    log.info("User ID: {}", userId);
                    log.info("Role: {}", role);
                    log.info("Subscription Tier: {}", tierSubscription);
                    log.info("Token Valid: true");
                    log.info("==========================");

                    // Bloquear usuarios FREE excepto para endpoints públicos
                    if ("FREE".equals(tierSubscription) && !isPublicEndpoint(request.getRequestURI())) {
                        log.warn("Access denied for FREE tier user: {} to endpoint: {}", username, request.getRequestURI());
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Tu plan FREE no permite acceso a esta funcionalidad. Actualiza a BASIC o PRO.\"}");
                        return false;
                    }

                    UserContext userContext = new UserContext(
                            userId,
                            username,
                            role,
                            tierSubscription,
                            jwtTokenUtil.extractExpiration(token)
                    );
                    
                    userContextService.setCurrentUser(userContext);
                    log.debug("User context set for user: {}", userContext.getUsuario());
                } else {
                    log.warn("Invalid JWT token received");
                }
            } catch (Exception e) {
                log.error("Error processing JWT token: {}", e.getMessage());
                // Continúa sin establecer el contexto de usuario
            }
        } else {
            log.debug("No JWT token found in Authorization header");
        }
        
        return true; // Continúa con la ejecución
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Limpia el contexto del usuario después de completar la request
        userContextService.clearCurrentUser();
    }

    /**
     * Determina si un endpoint es público y no requiere validación de tier.
     * Los proyectos de la plataforma pueden ser vistos por usuarios FREE.
     * 
     * @param requestURI la URI de la petición
     * @return true si es un endpoint público
     */
    private boolean isPublicEndpoint(String requestURI) {
        // Endpoints públicos para usuarios FREE
        return requestURI.equals("/api/v1/projects/platform") ||
               requestURI.startsWith("/swagger") ||
               requestURI.startsWith("/v3/api-docs") ||
               requestURI.equals("/actuator/health");
    }
}