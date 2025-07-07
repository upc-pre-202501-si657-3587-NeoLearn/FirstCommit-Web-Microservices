package com.neolearn.projects_service.project.domain.model.commands;

public record SendMessageCommand(
    Long idProyecto,
    Long idUsuarioEmisor,
    String contenido
) {
    public SendMessageCommand {
        if (idProyecto == null || idProyecto <= 0) {
            throw new IllegalArgumentException("El ID del proyecto debe ser válido");
        }
        if (idUsuarioEmisor == null || idUsuarioEmisor <= 0) {
            throw new IllegalArgumentException("El ID del usuario emisor debe ser válido");
        }
        if (contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede ser nulo o vacío");
        }
    }
} 