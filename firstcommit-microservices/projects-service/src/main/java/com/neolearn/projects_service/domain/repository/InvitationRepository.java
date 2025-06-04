package com.neolearn.projects_service.domain.repository;
import com.neolearn.projects_service.domain.model.Invitation;
import com.neolearn.projects_service.domain.model.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByProjectId(Long projectId);
    List<Invitation> findByInvitedUserIdAndStatus(Long invitedUserId, InvitationStatus status);
    Optional<Invitation> findByProjectIdAndInvitedUserId(Long projectId, Long invitedUserId);
}