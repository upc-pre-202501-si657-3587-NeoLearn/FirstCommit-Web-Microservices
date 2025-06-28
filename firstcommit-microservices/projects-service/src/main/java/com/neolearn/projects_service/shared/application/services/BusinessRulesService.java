package com.neolearn.projects_service.shared.application.services;

import com.neolearn.projects_service.project.domain.model.aggregates.Project;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.ProjectMemberRepository;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.ProjectRepository;
import com.neolearn.projects_service.shared.domain.exceptions.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BusinessRulesService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public BusinessRulesService(ProjectRepository projectRepository, 
                               ProjectMemberRepository projectMemberRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    /**
     * Valida que el tier del usuario permita acceso a la funcionalidad.
     * Los usuarios FREE no pueden realizar ninguna acción.
     * 
     * @param userTier el tier del usuario (FREE, BASIC, PRO)
     * @throws BusinessRuleException si el tier no tiene acceso
     */
    public void validateTierAccess(String userTier) {
        if ("FREE".equals(userTier)) {
            throw new BusinessRuleException("Tu plan FREE no tiene acceso a esta funcionalidad. Actualiza a BASIC o PRO para continuar.");
        }
        
        if (userTier == null || userTier.isBlank()) {
            throw new BusinessRuleException("No se pudo determinar tu plan de suscripción. Contacta al soporte.");
        }
        
        log.debug("Tier {} validado para acceso", userTier);
    }

    /**
     * Valida que el usuario no exceda el límite de proyectos según su tier.
     * BASIC: máximo 1 proyecto
     * PRO: máximo 3 proyectos
     * 
     * @param username el username del usuario
     * @param tier el tier del usuario
     * @throws BusinessRuleException si excede el límite
     */
    public void validateProjectLimit(String username, String tier) {
        long currentProjects = projectMemberRepository.countProjectsByUsername(username);
        int maxProjects = getMaxProjectsByTier(tier);
        
        log.debug("Usuario {} (tier: {}) tiene {} proyectos, máximo permitido: {}", 
                username, tier, currentProjects, maxProjects);
        
        if (currentProjects >= maxProjects) {
            throw new BusinessRuleException(
                String.format("Has alcanzado el límite de %d proyecto(s) para tu plan %s. " +
                             "Actualiza tu plan para unirte a más proyectos.", 
                             maxProjects, tier)
            );
        }
    }

    /**
     * Valida que el usuario sea el administrador (creador) del proyecto.
     * Solo el administrador puede modificar el proyecto, invitar miembros, etc.
     * 
     * @param projectId el ID del proyecto
     * @param username el username del usuario
     * @throws BusinessRuleException si no es el administrador
     */
    public void validateIsProjectAdmin(Long projectId, String username) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        
        if (projectOpt.isEmpty()) {
            throw new BusinessRuleException("El proyecto no existe.");
        }
        
        Project project = projectOpt.get();
        
        if (!project.getUsernameCreador().equals(username)) {
            throw new BusinessRuleException("Solo el administrador del proyecto puede realizar esta acción.");
        }
        
        log.debug("Usuario {} validado como administrador del proyecto {}", username, projectId);
    }

    /**
     * Valida que el proyecto no exceda el límite de miembros (máximo 5).
     * 
     * @param projectId el ID del proyecto
     * @throws BusinessRuleException si el proyecto ya tiene 5 miembros
     */
    public void validateMemberLimit(Long projectId) {
        long memberCount = projectMemberRepository.countByIdIdProyecto(projectId);
        final int MAX_MEMBERS = 5;
        
        log.debug("Proyecto {} tiene {} miembros, máximo permitido: {}", 
                projectId, memberCount, MAX_MEMBERS);
        
        if (memberCount >= MAX_MEMBERS) {
            throw new BusinessRuleException(
                String.format("El proyecto ha alcanzado el límite máximo de %d miembros.", MAX_MEMBERS)
            );
        }
    }

    /**
     * Valida que el usuario pueda unirse a un proyecto adicional según su tier.
     * Similar a validateProjectLimit pero con un mensaje más específico para invitaciones.
     * 
     * @param username el username del usuario invitado
     * @param tier el tier del usuario invitado
     * @throws BusinessRuleException si no puede unirse a más proyectos
     */
    public void validateCanJoinProject(String username, String tier) {
        long currentProjects = projectMemberRepository.countProjectsByUsername(username);
        int maxProjects = getMaxProjectsByTier(tier);
        
        if (currentProjects >= maxProjects) {
            throw new BusinessRuleException(
                String.format("El usuario no puede unirse al proyecto. Su plan %s permite máximo %d proyecto(s) " +
                             "y ya está en %d.", tier, maxProjects, currentProjects)
            );
        }
    }

    /**
     * Valida que un miembro puede ser removido del proyecto.
     * El administrador no puede ser removido (debe transferir administración primero).
     * 
     * @param projectId el ID del proyecto
     * @param usernameToRemove el username del miembro a remover
     * @param currentUsername el username del usuario que quiere remover
     * @throws BusinessRuleException si no se puede remover
     */
    public void validateCanRemoveMember(Long projectId, String usernameToRemove, String currentUsername) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        
        if (projectOpt.isEmpty()) {
            throw new BusinessRuleException("El proyecto no existe.");
        }
        
        Project project = projectOpt.get();
        
        // No se puede remover al administrador
        if (project.getUsernameCreador().equals(usernameToRemove)) {
            throw new BusinessRuleException("No se puede remover al administrador del proyecto.");
        }
        
        // Si no es el mismo usuario saliendo voluntariamente, debe ser admin
        if (!currentUsername.equals(usernameToRemove)) {
            validateIsProjectAdmin(projectId, currentUsername);
        }
        
        log.debug("Validación exitosa para remover miembro {} del proyecto {}", 
                usernameToRemove, projectId);
    }

    /**
     * Determina el número máximo de proyectos según el tier.
     * 
     * @param tier el tier del usuario
     * @return el número máximo de proyectos
     */
    private int getMaxProjectsByTier(String tier) {
        return switch (tier.toUpperCase()) {
            case "BASIC" -> 1;
            case "PRO" -> 3;
            case "FREE" -> 0; // Los FREE no deberían llegar aquí
            default -> {
                log.warn("Tier desconocido: {}. Asumiendo límite BASIC.", tier);
                yield 1;
            }
        };
    }
}