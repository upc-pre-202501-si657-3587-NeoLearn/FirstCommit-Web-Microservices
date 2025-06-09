package com.neolearn.projects_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "project_id", nullable = false) private Project project;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String type; // e.g., "LINK", "DOCUMENT_REFERENCE"
    @Lob @Column(nullable = false, columnDefinition = "TEXT") private String location;
    @Column(nullable = false) private Long uploadedByUserId;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}