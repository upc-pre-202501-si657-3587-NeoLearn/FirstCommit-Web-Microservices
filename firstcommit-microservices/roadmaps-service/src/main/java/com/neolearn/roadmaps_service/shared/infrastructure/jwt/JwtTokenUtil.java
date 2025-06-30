package com.neolearn.roadmaps_service.shared.infrastructure.jwt;

import com.neolearn.roadmaps_service.shared.infrastructure.configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtTokenUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        // Support both "id" (new format) and "userId" (legacy format) for compatibility
        Object userIdClaim = claims.get("id");
        if (userIdClaim == null) {
            userIdClaim = claims.get("userId"); // Fallback to legacy format
        }
        
        if (userIdClaim == null) {
            log.error("No user ID found in JWT token. Available claims: {}", claims.keySet());
            throw new RuntimeException("No user ID found in JWT token");
        }
        
        if (userIdClaim instanceof Integer) {
            return ((Integer) userIdClaim).longValue();
        } else if (userIdClaim instanceof Long) {
            return (Long) userIdClaim;
        } else if (userIdClaim instanceof String) {
            try {
                return Long.parseLong((String) userIdClaim);
            } catch (NumberFormatException e) {
                log.error("Cannot parse user ID string '{}' to Long", userIdClaim);
                throw new RuntimeException("Invalid user ID format in JWT token");
            }
        }
        
        log.error("User ID claim has unsupported type: {}", userIdClaim.getClass());
        throw new RuntimeException("Invalid user ID type in JWT token: " + userIdClaim.getClass());
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("rol", String.class);
    }

    public String extractTierSubscription(String token) {
        return extractAllClaims(token).get("tier_subscription", String.class);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return false;
        }
    }
}