package com.neolearn.projects_service.application.dto.invitation;
import com.neolearn.projects_service.domain.model.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor public class InvitationResponse {
    private Long id; private Long projectId; private Long invitedUserId;
    private Long inviterUserId; private InvitationStatus status;
    private LocalDateTime createdAt; private LocalDateTime respondedAt;
}