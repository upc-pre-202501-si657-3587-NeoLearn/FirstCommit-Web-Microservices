package com.neolearn.courses_service.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Recurso que representa el cuerpo de la petición para actualizar el progreso de un usuario en un curso.
 * @param lastContentCompletedId El identificador único del último contenido (lección, quiz) que el usuario ha completado.
 * @param progressPercentage El nuevo porcentaje de progreso total del curso para el usuario (0-100).
 */
public record UpdateProgressResource(

        @NotBlank(message = "El ID del último contenido completado no puede estar vacío.")
        String lastContentCompletedId,

        @NotNull(message = "El porcentaje de progreso no puede ser nulo.")
        @Min(value = 0, message = "El progreso no puede ser menor que 0.")
        @Max(value = 100, message = "El progreso no puede ser mayor que 100.")
        Integer progressPercentage

) {}