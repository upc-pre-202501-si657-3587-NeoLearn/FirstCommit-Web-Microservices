package com.neolearn.projects_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"members", "invitations", "resources", "tasks"})
@ToString(exclude = {"members", "invitations", "resources", "tasks"})
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 255) private String name;
    @Lob @Column(columnDefinition = "TEXT") private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 50) private ProjectType projectType;
    @Column(nullable = false) private Long ownerUserId;
    @Column(length = 100) private String license;
    @Column(length = 255, unique = true) private String sendbirdChannelId;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProjectMember> members = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Invitation> invitations = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Resource> resources = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public boolean isUserMember(Long userId) {
        if (userId == null) return false;
        return members.stream().anyMatch(m -> userId.equals(m.getUserId()));
    }
    public boolean isUserAdmin(Long userId) {
        if (userId == null) return false;
        return members.stream().anyMatch(m -> userId.equals(m.getUserId()) && m.getRole() == MemberRole.ADMIN);
    }
}