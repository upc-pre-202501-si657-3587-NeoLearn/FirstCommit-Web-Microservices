package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter
@NoArgsConstructor
public class Message extends AuditableModel {

    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @Column(name = "id_usuario_emisor", nullable = false)
    private Long idUsuarioEmisor;

    @Column(name = "contenido", nullable = false, length = 1000)
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    public Message(Long idProyecto, Long idUsuarioEmisor, String contenido) {
        this.idProyecto = idProyecto;
        this.idUsuarioEmisor = idUsuarioEmisor;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }
} 