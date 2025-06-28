package com.neolearn.projects_service.project.domain.model.queries;

public record GetUserTasksQuery(
    Long idUsuario,
    Long idProyecto
) {
    public GetUserTasksQuery {
        if (idUsuario == null || idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser válido");
        }
        // idProyecto puede ser nulo si se quieren obtener todas las tareas del usuario
    }
} 