package com.neolearn.projects_service.shared.infrastructure.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserContextService {

    private static final ThreadLocal<UserContext> userContextHolder = new ThreadLocal<>();

    public void setCurrentUser(UserContext userContext) {
        userContextHolder.set(userContext);
        log.debug("User context set: {}", userContext.getUsuario());
    }

    public UserContext getCurrentUser() {
        return userContextHolder.get();
    }

    public Long getCurrentUserId() {
        UserContext context = getCurrentUser();
        return context != null ? context.getId() : null;
    }

    public String getCurrentUsername() {
        UserContext context = getCurrentUser();
        return context != null ? context.getUsuario() : null;
    }

    public String getCurrentUserRole() {
        UserContext context = getCurrentUser();
        return context != null ? context.getRol() : null;
    }

    public String getCurrentUserTier() {
        UserContext context = getCurrentUser();
        return context != null ? context.getTierSubscription() : null;
    }

    public boolean isUserAuthenticated() {
        UserContext context = getCurrentUser();
        return context != null && !context.isExpired();
    }

    public boolean hasRole(String role) {
        UserContext context = getCurrentUser();
        return context != null && context.hasRole(role);
    }

    public boolean hasTier(String tier) {
        UserContext context = getCurrentUser();
        return context != null && context.hasTier(tier);
    }

    public boolean hasTierOrHigher(String minimumTier) {
        UserContext context = getCurrentUser();
        return context != null && context.hasTierOrHigher(minimumTier);
    }

    public void clearCurrentUser() {
        userContextHolder.remove();
        log.debug("User context cleared");
    }

    public boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }
}