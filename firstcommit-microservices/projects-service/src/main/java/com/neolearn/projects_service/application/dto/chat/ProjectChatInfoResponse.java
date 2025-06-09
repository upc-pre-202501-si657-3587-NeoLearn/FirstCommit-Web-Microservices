package com.neolearn.projects_service.application.dto.chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectChatInfoResponse {
    private String projectId; private String sendbirdChannelId;
    private String sendbirdUserId; private String sendbirdUserAccessToken;
}