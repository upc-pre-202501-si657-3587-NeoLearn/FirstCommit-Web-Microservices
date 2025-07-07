package com.neolearn.projects_service.shared.application.services;

import com.neolearn.projects_service.project.domain.model.entities.User;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.UserRepository;
import com.neolearn.projects_service.shared.infrastructure.jwt.UserContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserSyncService {

    private final UserRepository userRepository;
    private final UserContextService userContextService;

    public UserSyncService(UserRepository userRepository, UserContextService userContextService) {
        this.userRepository = userRepository;
        this.userContextService = userContextService;
    }

    /**
     * Asegura que un usuario existe en la base de datos local.
     * Si no existe, lo crea automáticamente usando los datos del JWT.
     * 
     * @param username el username del usuario
     * @return el usuario existente o recién creado
     */
    public User ensureUserExists(String username) {
        Optional<User> existingUser = userRepository.findByNombreUsuario(username);
        
        if (existingUser.isPresent()) {
            // Actualizar datos del JWT por si cambiaron
            User user = existingUser.get();
            updateUserFromJWT(user);
            return userRepository.save(user);
        } else {
            // Crear nuevo usuario con datos del JWT
            return createUserFromJWT(username);
        }
    }

    /**
     * Crea un nuevo usuario usando la información del JWT token actual.
     * 
     * @param username el username del usuario
     * @return el usuario recién creado
     */
    private User createUserFromJWT(String username) {
        String role = userContextService.getCurrentUserRole();
        String tier = userContextService.getCurrentUserTier();
        
        log.info("Creando nuevo usuario: {} con rol: {} y tier: {}", username, role, tier);
        
        // Crear usuario con email temporal basado en username
        User newUser = new User(username, username + "@temp.com", role, tier);
        
        return userRepository.save(newUser);
    }

    /**
     * Actualiza los datos del usuario con la información más reciente del JWT.
     * 
     * @param user el usuario a actualizar
     */
    private void updateUserFromJWT(User user) {
        String currentRole = userContextService.getCurrentUserRole();
        String currentTier = userContextService.getCurrentUserTier();
        
        // Actualizar solo si los datos han cambiado
        boolean updated = false;
        
        if (currentRole != null && !currentRole.equals(user.getRol())) {
            user.updateRol(currentRole);
            updated = true;
        }
        
        if (currentTier != null && !currentTier.equals(user.getTierSubscription())) {
            user.updateTierSubscription(currentTier);
            updated = true;
        }
        
        if (updated) {
            log.info("Actualizando datos del usuario: {} - rol: {}, tier: {}", 
                    user.getNombreUsuario(), currentRole, currentTier);
        }
    }

    /**
     * Obtiene un usuario por username, creándolo si no existe.
     * 
     * @param username el username del usuario
     * @return Optional con el usuario encontrado o creado
     */
    public Optional<User> findOrCreateUser(String username) {
        try {
            return Optional.of(ensureUserExists(username));
        } catch (Exception e) {
            log.error("Error al crear/encontrar usuario: {}", username, e);
            return Optional.empty();
        }
    }
}