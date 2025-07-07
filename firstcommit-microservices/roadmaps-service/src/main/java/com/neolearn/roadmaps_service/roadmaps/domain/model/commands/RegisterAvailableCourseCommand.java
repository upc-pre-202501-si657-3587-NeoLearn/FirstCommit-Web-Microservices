package com.neolearn.roadmaps_service.roadmaps.domain.model.commands;

// Usamos un record para un comando inmutable y simple
public record RegisterAvailableCourseCommand(
        String externalCourseId,
        String title
) {}