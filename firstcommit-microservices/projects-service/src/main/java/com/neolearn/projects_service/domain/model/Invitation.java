package com.neolearn.projects_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invitations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "project_id", nullable = false) private Project project;
    @Column(nullable = false) private Long invitedUserId;
    @Column(nullable = false) private Long inviterUserId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private InvitationStatus status;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); status = InvitationStatus.PENDING; }
}