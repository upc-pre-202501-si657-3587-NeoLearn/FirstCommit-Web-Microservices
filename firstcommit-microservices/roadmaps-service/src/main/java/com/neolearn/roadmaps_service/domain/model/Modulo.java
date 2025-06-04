package com.neolearn.roadmaps_service.domain.model;

import com.neolearn.roadmaps_service.infrastructure.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Modulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagen;
    private String nombre;
    private String descripcion;

    @Column(name = "course_ids")
    @Convert(converter = ListToJsonConverter.class)
    private List<Long> courseIds;
}
