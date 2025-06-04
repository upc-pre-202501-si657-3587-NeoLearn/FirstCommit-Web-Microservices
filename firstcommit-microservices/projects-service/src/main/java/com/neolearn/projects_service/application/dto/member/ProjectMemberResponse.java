package com.neolearn.projects_service.application.dto.member;
import com.neolearn.projects_service.domain.model.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor public class ProjectMemberResponse {
    private Long id; private Long projectId; private Long userId;
    private MemberRole role; private LocalDateTime joinedAt;
}