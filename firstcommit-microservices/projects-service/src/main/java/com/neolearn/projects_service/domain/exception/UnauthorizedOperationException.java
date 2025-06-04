package com.neolearn.projects_service.domain.exception;
public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String message) { super(message); }
}