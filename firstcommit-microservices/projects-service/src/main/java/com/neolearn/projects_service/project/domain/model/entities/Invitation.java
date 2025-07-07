package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.project.domain.model.valueobjects.InvitationStatus;
import com.neolearn.projects_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitaciones")
@Getter
@NoArgsConstructor
public class Invitation extends AuditableModel {

    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @Column(name = "id_usuario_invitado", nullable = false)
    private Long idUsuarioInvitado;

    @Column(name = "id_usuario_invitador", nullable = false)
    private Long idUsuarioInvitador;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private InvitationStatus estado;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    public Invitation(Long idProyecto, Long idUsuarioInvitado, Long idUsuarioInvitador) {
        this.idProyecto = idProyecto;
        this.idUsuarioInvitado = idUsuarioInvitado;
        this.idUsuarioInvitador = idUsuarioInvitador;
        this.estado = InvitationStatus.PENDIENTE;
        this.fechaEnvio = LocalDateTime.now();
    }

    public void aceptar() {
        this.estado = InvitationStatus.ACEPTADA;
    }

    public void rechazar() {
        this.estado = InvitationStatus.RECHAZADA;
    }

    public boolean isPendiente() {
        return this.estado == InvitationStatus.PENDIENTE;
    }
} 