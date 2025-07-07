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

    @Lob
    private String theoryContent;
    @Lob
    private String quizContent;
    @Lob
    private String codingContent;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected CourseContent() {}

    public CourseContent(String theory, String quiz, String coding) {
        if (theory == null || theory.isBlank()) throw new IllegalArgumentException("Theory content cannot be empty.");
        if (quiz == null || quiz.isBlank()) throw new IllegalArgumentException("Quiz content cannot be empty.");
        if (coding == null || coding.isBlank()) throw new IllegalArgumentException("Coding content cannot be empty.");

        validateJsonStructure("Theory", theory);
        validateJsonStructure("Quiz", quiz);
        validateJsonStructure("Coding", coding);

        this.theoryContent = theory;
        this.quizContent = quiz;
        this.codingContent = coding;
    }

    private void validateJsonStructure(String fieldName, String jsonContent) {
        try {
            objectMapper.readTree(jsonContent);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(fieldName + " content is not a valid JSON: " + e.getMessage());
        }
    }

    // ====================================================================
    // --- NUEVOS MÉTODOS PARA AÑADIR COMPORTAMIENTO ---
    // ====================================================================

    /**
     * Devuelve el número de elementos en el JSON de teoría.
     * Asume que el JSON es un array.
     * @return el número de elementos, o 0 si el JSON no es un array.
     */
    public int getTheoryItemsCount() {
        return countJsonArrayElements(this.theoryContent);
    }

    /**
     * Devuelve el número de preguntas en el JSON de quizzes.
     * Asume que el JSON es un array.
     * @return el número de preguntas, o 0 si el JSON no es un array.
     */
    public int getQuizItemsCount() {
        return countJsonArrayElements(this.quizContent);
    }

    /**
     * Devuelve el número de ejercicios en el JSON de coding.
     * Asume que el JSON es un array.
     * @return el número de ejercicios, o 0 si el JSON no es un array.
     */
    public int getCodingItemsCount() {
        return countJsonArrayElements(this.codingContent);
    }

    /**
     * Método de ayuda privado para parsear un string JSON y contar los elementos si es un array.
     * @param jsonContent El string JSON a parsear.
     * @return El número de elementos, o 0 en caso de error o si no es un array.
     */
    private int countJsonArrayElements(String jsonContent) {
        if (jsonContent == null || jsonContent.isBlank()) {
            return 0;
        }
        try {
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            if (rootNode.isArray()) {
                return rootNode.size();
            }
        } catch (JsonProcessingException e) {
            // Si el JSON es inválido (aunque el constructor ya lo valida), devolvemos 0.
            return 0;
        }
        // Si el JSON es válido pero no es un array (ej. es un objeto), devolvemos 0.
        return 0;
    }
}