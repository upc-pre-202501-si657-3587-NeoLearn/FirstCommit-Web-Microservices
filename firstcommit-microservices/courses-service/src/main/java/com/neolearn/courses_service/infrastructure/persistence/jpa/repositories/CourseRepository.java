package com.neolearn.courses_service.infrastructure.persistence.jpa.repositories;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Busca cursos por instructor y título (para validar duplicados)
    Optional<Course> findByTitleAndInstructorId(String title, String instructorId);

    // Busca cursos por ID e instructor (para validar ownership)
    Optional<Course> findByIdAndInstructorId(Long id, String instructorId);

    // Filtra cursos publicados
    List<Course> findByPublishedTrue();

    @Query("SELECT c FROM Course c JOIN c.enrolledStudents e WHERE e = :userId")
    List<Course> findByEnrolledUser(@Param("userId") String userId);

    // Filtra cursos donde el usuario está inscrito
    @Query("SELECT DISTINCT c FROM Course c JOIN c.enrolledStudents e WHERE e = :userId")
    List<Course> findCoursesByEnrolledUser(@Param("userId") String userId);

    // Obtiene el progreso de un usuario en un curso específico
    @Query(value = """
    SELECT progress 
    FROM courses_user_progress 
    WHERE course_id = :courseId 
    AND user_id = :userId
    """, nativeQuery = true)
    Optional<Integer> findUserProgress(
            @Param("courseId") Long courseId,
            @Param("userId") String userId);
}