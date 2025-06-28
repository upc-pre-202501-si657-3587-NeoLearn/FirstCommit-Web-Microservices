package com.neolearn.iam_service.iam.infrastructure.tokens.jwt.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.neolearn.iam_service.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.neolearn.iam_service.iam.infrastructure.tokens.jwt.BearerTokenService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Token service implementation for JWT tokens.
 * This class is responsible for generating and validating JWT tokens.
 * It uses the secret and expiration days from the application.properties file.
 */
@Service
public class TokenServiceImpl implements BearerTokenService {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_BEGIN_INDEX = 7;

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value("${authorization.jwt.expiration.days}")
    private int expirationDays;

    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String parameter = getAuthorizationParameterFrom(request);
        if (isTokenPresentIn(parameter) && isBearerTokenIn(parameter))
            return extractTokenFrom(parameter);
        return null;
    }

    /**
     * This method generates a JWT token from an authentication object
     * @param authentication the authentication object
     * @return String the JWT token
     * @see Authentication
     */
    @Override
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        Long userId = userDetailsImpl.getId(); // Get the user's ID from UserDetailsImpl
        
        // Extract user data from authentication
        return buildTokenWithUserData(userDetails.getUsername(), userId, userDetailsImpl);
    }

    /**
     * This method generates a JWT token from a username
     * @param username the username
     * @return String the JWT token
     */
    @Override
    public String generateToken(String username) {
        // This method might need to be updated to include the user's ID
        return buildTokenWithDefaultParameters(username, null);
    }

    /**
     * This method generates a JWT token from a User entity with complete information
     * @param user the user entity
     * @return String the JWT token
     */
    @Override
    public String generateToken(com.neolearn.iam_service.iam.domain.model.aggregates.User user) {
        var issuedAt = new Date();
        var expiration = DateUtils.addDays(issuedAt, expirationDays);
        var key = getSigningKey();

        // Extract role from user (take the first one, or default to ROLE_USER)
        String role = user.getRoles().stream()
                .map(r -> r.getStringName())
                .findFirst()
                .orElse("ROLE_USER");

        // Extract subscription tier from user (or use default FREE)
        String subscriptionTier = user.getSubscriptionTier() != null ? 
                user.getSubscriptionTier().name() : "FREE";

        // Log the encoded information
        LOGGER.info("=== JWT TOKEN ENCODING (User Entity) ===");
        LOGGER.info("Username: {}", user.getUsername());
        LOGGER.info("User ID: {}", user.getId());
        LOGGER.info("Role: {}", role);
        LOGGER.info("Subscription Tier: {}", subscriptionTier);
        LOGGER.info("Issued At: {}", issuedAt);
        LOGGER.info("Expires At: {}", expiration);
        LOGGER.info("========================================");

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("rol", role)
                .claim("tier_subscription", subscriptionTier)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * This method extracts the username from a JWT token
     * @param token the token
     * @return String the username
     */
    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This method validates a JWT token
     * @param token the token
     * @return boolean true if the token is valid, false otherwise
     */
    @Override
    public boolean validateToken(String token) {
        try {
            // Versión corregida utilizando parserBuilder() en lugar de parser().verifyWith()
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            LOGGER.info("JSON Web Token is valid");
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JSON Web Token signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JSON Web Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JSON Web Token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JSON Web Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JSON Web Token claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // private methods

    /**
     * Get the signing key
     * @return SecretKey the signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * This method generates a JWT token from a username and a secret.
     * It uses the default expiration days from the application.properties file.
     * @param username the username
     * @return String the JWT token
     */
    private String buildTokenWithDefaultParameters(String username, Long userId) {
        var issuedAt = new Date();
        var expiration = DateUtils.addDays(issuedAt, expirationDays);
        var key = getSigningKey();

        // Log the encoded information (simple token generation)
        LOGGER.info("=== JWT TOKEN ENCODING (Simple) ===");
        LOGGER.info("Username: {}", username);
        LOGGER.info("User ID: {}", userId);
        LOGGER.info("Role: ROLE_USER (default)");
        LOGGER.info("Subscription Tier: FREE (default)");
        LOGGER.info("Issued At: {}", issuedAt);
        LOGGER.info("Expires At: {}", expiration);
        LOGGER.info("===================================");

        // Versión corregida utilizando setSubject() en lugar de subject()
        return Jwts.builder()
                .setSubject(username)
                .claim("id", userId) // Changed from "userId" to "id" for compatibility
                .claim("rol", "ROLE_USER") // Default role
                .claim("tier_subscription", "FREE") // Default tier
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * This method generates a JWT token with real user data extracted from UserDetailsImpl.
     * @param username the username
     * @param userId the user ID
     * @param userDetails the user details implementation
     * @return String the JWT token
     */
    private String buildTokenWithUserData(String username, Long userId, UserDetailsImpl userDetails) {
        var issuedAt = new Date();
        var expiration = DateUtils.addDays(issuedAt, expirationDays);
        var key = getSigningKey();

        // Extract role from authorities (take the first one, or default to ROLE_USER)
        String role = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        // For now, use default tier - this should be extracted from User entity in the future
        String subscriptionTier = "FREE"; // TODO: Extract from User entity when available

        // Log the encoded information
        LOGGER.info("=== JWT TOKEN ENCODING ===");
        LOGGER.info("Username: {}", username);
        LOGGER.info("User ID: {}", userId);
        LOGGER.info("Role: {}", role);
        LOGGER.info("Subscription Tier: {}", subscriptionTier);
        LOGGER.info("Issued At: {}", issuedAt);
        LOGGER.info("Expires At: {}", expiration);
        LOGGER.info("=========================");

        return Jwts.builder()
                .setSubject(username)
                .claim("id", userId)
                .claim("rol", role)
                .claim("tier_subscription", subscriptionTier)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * Extract all claims from a token
     * @param token the token
     * @return Claims the claims
     */
    private Claims extractAllClaims(String token) {
        // Versión corregida utilizando parserBuilder() en lugar de parser().verifyWith()
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract a claim from a token
     * @param token the token
     * @param claimsResolver the claims resolver
     * @param <T> the type of the claim
     * @return T the claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenPresentIn(String authorizationParameter) {
        return StringUtils.hasText(authorizationParameter);
    }

    private boolean isBearerTokenIn(String authorizationParameter) {
        return authorizationParameter.startsWith(BEARER_TOKEN_PREFIX);
    }

    private String extractTokenFrom(String authorizationHeaderParameter) {
        return authorizationHeaderParameter.substring(TOKEN_BEGIN_INDEX);
    }

    private String getAuthorizationParameterFrom(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_PARAMETER_NAME);
    }
}