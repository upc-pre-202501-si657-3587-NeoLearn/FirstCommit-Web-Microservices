package com.neolearn.courses_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Arrays;

@Embeddable
@Getter
public class CourseContent {
    private String theoryContent;  // JSON con videos/texto
    private String quizContent;    // JSON con preguntas
    private String codingContent;  // JSON con ejercicios

    public CourseContent() {

    }

    public CourseContent(String theory, String quiz, String coding) {
        validateContent(theory, quiz, coding);
        this.theoryContent = theory;
        this.quizContent = quiz;
        this.codingContent = coding;
    }

    private void validateContent(String... contents) {
        Arrays.stream(contents).forEach(content -> {
            if (content == null || content.isBlank()) {
                throw new IllegalArgumentException("Content cannot be empty");
            }
            // Validar estructura JSON aquí
        });
    }
}