package com.neolearn.projects_service.project.domain.model.entities;

import com.neolearn.projects_service.project.domain.model.valueobjects.ResourceType;
import com.neolearn.projects_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recursos")
@Getter
@NoArgsConstructor
public class Resource extends AuditableModel {

    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @Column(name = "id_usuario_que_comparte", nullable = false)
    private Long idUsuarioQueComparte;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recurso", nullable = false)
    private ResourceType tipoRecurso;

    @Column(name = "valor_recurso", nullable = false, length = 1000)
    private String valorRecurso;

    @Column(name = "nombre_visible", nullable = false)
    private String nombreVisible;

    @Column(name = "fecha_compartido", nullable = false)
    private LocalDateTime fechaCompartido;

    public Resource(Long idProyecto, Long idUsuarioQueComparte, ResourceType tipoRecurso, 
                   String valorRecurso, String nombreVisible) {
        this.idProyecto = idProyecto;
        this.idUsuarioQueComparte = idUsuarioQueComparte;
        this.tipoRecurso = tipoRecurso;
        this.valorRecurso = valorRecurso;
        this.nombreVisible = nombreVisible;
        this.fechaCompartido = LocalDateTime.now();
    }

    public void updateValorRecurso(String valorRecurso) {
        this.valorRecurso = valorRecurso;
    }

    public void updateNombreVisible(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }
} 