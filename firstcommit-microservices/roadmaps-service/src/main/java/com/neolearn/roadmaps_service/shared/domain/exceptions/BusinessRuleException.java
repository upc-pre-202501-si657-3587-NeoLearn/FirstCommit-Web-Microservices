package com.neolearn.roadmaps_service.shared.domain.exceptions;

/**
 * Excepción personalizada para violaciones de reglas de negocio.
 * Se lanza cuando un usuario intenta realizar una acción que no está permitida
 * según las reglas de negocio definidas (límites de tier, permisos, etc.).
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}