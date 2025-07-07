package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Getter
@NoArgsConstructor
public class User extends AuditableModel {

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId; // Este es el campo que faltaba

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "rol")
    private String rol;

    @Column(name = "tier_subscription")
    private String tierSubscription;

    public User(String nombreUsuario, String email) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
    }

    public User(String nombreUsuario, String email, String rol, String tierSubscription) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.rol = rol;
        this.tierSubscription = tierSubscription;
    }

    public void updateNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateRol(String rol) {
        this.rol = rol;
    }

    public void updateTierSubscription(String tierSubscription) {
        this.tierSubscription = tierSubscription;
    }
} 