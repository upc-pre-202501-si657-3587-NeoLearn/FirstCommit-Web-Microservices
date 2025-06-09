package com.neolearn.projects_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_members", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "project_id", nullable = false) private Project project;
    @Column(name = "user_id", nullable = false) private Long userId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private MemberRole role;
    @Column(nullable = false, updatable = false) private LocalDateTime joinedAt;
    @PrePersist protected void onJoin() { joinedAt = LocalDateTime.now(); }
}