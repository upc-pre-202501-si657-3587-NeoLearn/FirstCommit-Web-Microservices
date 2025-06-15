// Ubicación: src/main/java/com/neolearn/courses_service/domain/model/valueobjects/CourseContent.java
package com.neolearn.courses_service.domain.model.valueobjects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;

@Embeddable
@Getter
public class CourseContent {

    // @Lob podría ser necesario si el contenido es muy grande
    @Lob
    private String theoryContent;  // JSON con videos/texto
    @Lob
    private String quizContent;    // JSON con preguntas
    @Lob
    private String codingContent;  // JSON con ejercicios

    // ObjectMapper es thread-safe y reutilizable
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected CourseContent() {}

    public CourseContent(String theory, String quiz, String coding) {
        // Validamos que el contenido no sea nulo o vacío
        if (theory == null || theory.isBlank()) throw new IllegalArgumentException("Theory content cannot be empty.");
        if (quiz == null || quiz.isBlank()) throw new IllegalArgumentException("Quiz content cannot be empty.");
        if (coding == null || coding.isBlank()) throw new IllegalArgumentException("Coding content cannot be empty.");

        // Validamos que sea un JSON válido
        validateJsonStructure("Theory", theory);
        validateJsonStructure("Quiz", quiz);
        validateJsonStructure("Coding", coding);

        this.theoryContent = theory;
        this.quizContent = quiz;
        this.codingContent = coding;
    }

    /**
     * Valida que un string sea un JSON bien formado.
     * Para una validación más profunda (JSON Schema), se necesitarían librerías adicionales.
     */
    private void validateJsonStructure(String fieldName, String jsonContent) {
        try {
            objectMapper.readTree(jsonContent);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(fieldName + " content is not a valid JSON: " + e.getMessage());
        }
    }
}