// Ubicación: src/main/java/com/neolearn/courses_service/interfaces/rest/resources/UpdateCourseResource.java
package com.neolearn.courses_service.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Recurso para la actualización de un curso.
 * Contiene únicamente los campos que pueden ser modificados por el cliente.
 * El ID del curso se obtiene de la URL y el ID del instructor se gestiona internamente.
 */
public record UpdateCourseResource(
        @NotBlank @Size(max = 100)
        String title,

        @NotBlank
        String description,

        @NotBlank
        String category,

        // Asumimos que estos son strings que contienen JSON. Si no, ajustar el tipo.
        String theoryContent,
        String quizContent,
        String codingContent
) {}