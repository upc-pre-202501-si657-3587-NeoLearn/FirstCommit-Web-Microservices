package com.neolearn.iam_service.iam.application.internal.outboundservices.tokens;

import com.neolearn.iam_service.iam.domain.model.aggregates.User;

/**
 * TokenService interface
 * This interface is used to generate and validate tokens
 */
public interface TokenService {

    /**
     * Generate a token for a given username
     * @param username the username
     * @return String the token
     */
    String generateToken(String username);

    /**
     * Generate a token for a given user with complete information
     * @param user the user entity
     * @return String the token
     */
    String generateToken(User user);

    /**
     * Extract the username from a token
     * @param token the token
     * @return String the username
     */
    String getUsernameFromToken(String token);

    /**
     * Validate a token
     * @param token the token
     * @return boolean true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}