package com.neolearn.projects_service.domain.repository;
import com.neolearn.projects_service.domain.model.Project;
import com.neolearn.projects_service.domain.model.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProjectType(ProjectType projectType);
    List<Project> findByOwnerUserId(Long ownerUserId);
}