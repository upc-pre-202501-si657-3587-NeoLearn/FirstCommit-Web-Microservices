package com.neolearn.projects_service.domain.exception;
public class ProjectOperationException extends RuntimeException {
    public ProjectOperationException(String message) { super(message); }
    public ProjectOperationException(String message, Throwable cause) { super(message, cause); }
}