package com.neolearn.projects_service.interfaces.advice;

import com.neolearn.projects_service.domain.exception.ProjectOperationException;
import com.neolearn.projects_service.domain.exception.ResourceNotFoundException;
import com.neolearn.projects_service.domain.exception.UnauthorizedOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedOperationException(UnauthorizedOperationException ex) {
        logger.warn("Unauthorized operation: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ProjectOperationException.class)
    public ResponseEntity<Map<String, String>> handleProjectOperationException(ProjectOperationException ex) {
        logger.warn("Project operation failed: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        logger.warn("Validation errors: {}", errors);
        return new ResponseEntity<>(Map.of("message", "Validation failed", "errors", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        logger.error("Unsupported operation: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", "This operation is not currently supported: " + ex.getMessage()), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(Map.of("error", "An unexpected internal server error occurred. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}