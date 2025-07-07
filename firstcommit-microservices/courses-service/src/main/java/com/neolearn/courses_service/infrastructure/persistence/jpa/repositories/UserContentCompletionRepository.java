package com.neolearn.courses_service.infrastructure.persistence.jpa.repositories;

import com.neolearn.courses_service.domain.model.entities.UserContentCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContentCompletionRepository extends JpaRepository<UserContentCompletion, Long> {


    long countByUserIdAndCourseId(String userId, Long courseId);
}
