// Ubicación: src/main/java/com/neolearn/courses_service/infrastructure/persistence/jpa/repositories/UserProgressRepository.java
package com.neolearn.courses_service.infrastructure.persistence.jpa.repositories;

import com.neolearn.courses_service.domain.model.entities.UserProgress;
import com.neolearn.courses_service.domain.model.entities.UserProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, UserProgressId> {

    Optional<UserProgress> findByCourseIdAndUserId(Long courseId, String userId);
}