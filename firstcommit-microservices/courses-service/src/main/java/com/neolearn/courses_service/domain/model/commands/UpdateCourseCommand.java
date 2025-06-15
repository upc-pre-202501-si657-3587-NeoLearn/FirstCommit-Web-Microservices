
package com.neolearn.courses_service.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Comando para actualizar un curso.
 * Agrega toda la información necesaria para que el servicio de aplicación realice la tarea.
 */
public record UpdateCourseCommand(
        @NotNull
        Long courseId,          // Viene de la URL

        @NotBlank
        String instructorId,    // Viene del backend (temporalmente)

        @NotBlank @Size(max = 100)
        String title,

        @NotBlank
        String description,

        @NotBlank
        String category,

        String theoryContent,
        String quizContent,
        String codingContent
) {}