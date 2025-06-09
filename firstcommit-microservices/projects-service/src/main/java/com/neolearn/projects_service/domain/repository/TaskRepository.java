package com.neolearn.projects_service.domain.repository;
import com.neolearn.projects_service.domain.model.Task;
import com.neolearn.projects_service.domain.model.TaskStatus; // Import faltante
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByProjectIdAndAssignedToUserId(Long projectId, Long assignedToUserId);
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
}