package com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories;

import com.neolearn.projects_service.project.domain.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByExternalId(String externalId);
    Optional<User> findByEmail(String email);
    Optional<User> findByNombreUsuario(String NombreUsuario);
    boolean existsByEmail(String email);
    boolean existsByNombreUsuario(String NombreUsuario);
}