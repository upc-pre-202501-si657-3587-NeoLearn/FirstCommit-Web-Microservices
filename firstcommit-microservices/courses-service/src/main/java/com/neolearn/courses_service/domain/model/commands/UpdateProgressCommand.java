package com.neolearn.courses_service.domain.model.commands;

// Un 'record' es ideal para un comando inmutable.
public record UpdateProgressCommand(
        String userId,
        Long courseId,
        String lastContentCompletedId, // ID único del video/quiz/ejercicio completado
        int progressPercentage // El nuevo porcentaje de progreso total
) {}