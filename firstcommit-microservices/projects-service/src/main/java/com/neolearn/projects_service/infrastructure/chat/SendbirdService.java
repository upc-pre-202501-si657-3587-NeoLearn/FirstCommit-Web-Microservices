package com.neolearn.projects_service.infrastructure.chat;

import com.neolearn.projects_service.domain.exception.ProjectOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class SendbirdService {

    private static final Logger logger = LoggerFactory.getLogger(SendbirdService.class);

    @Value("${sendbird.app.id}")
    private String sendbirdAppId;

    @Value("${sendbird.api.token}")
    private String sendbirdApiToken;

    private final RestTemplate restTemplate;
    private final String SENDBIRD_API_BASE_URL_V3;

    public SendbirdService(RestTemplate restTemplate, @Value("${sendbird.app.id}") String appId) {
        this.restTemplate = restTemplate;
        if (appId == null || appId.isBlank() || appId.equals("TU_SENDBIRD_APP_ID") || appId.equals("${sendbird.app.id}")) {
            this.SENDBIRD_API_BASE_URL_V3 = "https://api-placeholder.sendbird.com/v3";
            logger.warn("Sendbird App ID not configured properly in application.properties, using placeholder API URL: {}", this.SENDBIRD_API_BASE_URL_V3);
        } else {
            this.SENDBIRD_API_BASE_URL_V3 = "https://api-" + appId + ".sendbird.com/v3";
        }
    }

    private HttpHeaders createApiHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (sendbirdApiToken == null || sendbirdApiToken.isBlank() || sendbirdApiToken.equals("TU_SENDBIRD_API_TOKEN") || sendbirdApiToken.equals("${sendbird.api.token}")) {
            logger.warn("Sendbird API Token not configured properly in application.properties. API calls will likely fail.");
            // Podrías incluso lanzar una excepción aquí si el token es esencial y no está configurado.
        }
        headers.set("Api-Token", sendbirdApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public void ensureUserExistsInSendbird(String platformUserId, String nickname, String profileUrl) {
        String sendbirdUserId = "neolearn_user_" + platformUserId;
        // String url = SENDBIRD_API_BASE_URL_V3 + "/users/" + sendbirdUserId;
        // HttpEntity<String> entity = new HttpEntity<>(createApiHeaders());
        logger.info("[MOCK] ensureUserExistsInSendbird: Attempting for Sendbird user: {}", sendbirdUserId);
        // Simulación:
        // try {
        //     restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        //     logger.info("[MOCK] Sendbird user {} already exists.", sendbirdUserId);
        // } catch (HttpClientErrorException.NotFound ex) {
        //     logger.info("[MOCK] Sendbird user {} not found, (would be) creating...", sendbirdUserId);
        //     // String createUserUrl = SENDBIRD_API_BASE_URL_V3 + "/users";
        //     // Map<String, Object> body = Map.of("user_id", sendbirdUserId, "nickname", nickname, "profile_url", profileUrl != null ? profileUrl : "");
        //     // HttpEntity<Map<String, Object>> createEntity = new HttpEntity<>(body, createApiHeaders());
        //     // restTemplate.postForEntity(createUserUrl, createEntity, String.class);
        //     // logger.info("[MOCK] Sendbird user {} (would be) created.", sendbirdUserId);
        // } catch (Exception e) {
        //     logger.error("[MOCK] Error during ensureUserExistsInSendbird for {}: {}", sendbirdUserId, e.getMessage());
        // }
    }

    public String createGroupChannelForProject(String projectName, Long platformProjectId, List<String> platformAdminUserIds, String projectImageUrl) {
        String channelUrl = "neolearn_project_channel_" + platformProjectId + "_" + UUID.randomUUID().toString().substring(0, 8);
        // List<String> sendbirdAdminUserIds = platformAdminUserIds.stream().map(id -> "neolearn_user_" + id).toList();
        // String url = SENDBIRD_API_BASE_URL_V3 + "/group_channels";
        // Map<String, Object> body = Map.of( /* ... */ );
        // HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, createApiHeaders());
        logger.info("[MOCK] createGroupChannelForProject: Attempting for project ID {}. Generated channel URL: {}", platformProjectId, channelUrl);
        // try {
        //     ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        //     // ...
        //     return createdChannelUrl;
        // } catch (Exception e) {
        //     logger.error("[MOCK] Error creating Sendbird channel for project {}: {}", platformProjectId, e.getMessage());
        //     throw new ProjectOperationException("[MOCK] Error creating Sendbird channel: " + e.getMessage());
        // }
        return channelUrl; // Devolver la URL generada para el mock
    }

    public void addUserToChannel(String channelUrl, String platformUserId) {
        String sendbirdUserId = "neolearn_user_" + platformUserId;
        // String url = SENDBIRD_API_BASE_URL_V3 + "/group_channels/" + channelUrl + "/invite";
        // Map<String, List<String>> body = Map.of("user_ids", List.of(sendbirdUserId));
        // HttpEntity<Map<String, List<String>>> entity = new HttpEntity<>(body, createApiHeaders());
        logger.info("[MOCK] addUserToChannel: Attempting to add user {} to Sendbird channel {}", sendbirdUserId, channelUrl);
        // try {
        //     restTemplate.postForEntity(url, entity, String.class);
        //     logger.info("[MOCK] User {} (would be) invited to channel {}.", sendbirdUserId, channelUrl);
        // } catch (Exception e) {
        //     logger.error("[MOCK] Error adding user {} to Sendbird channel {}: {}", sendbirdUserId, channelUrl, e.getMessage());
        // }
    }

    public void removeUserFromChannel(String channelUrl, String platformUserId) {
        String sendbirdUserId = "neolearn_user_" + platformUserId;
        // String url = SENDBIRD_API_BASE_URL_V3 + "/group_channels/" + channelUrl + "/leave";
        // Map<String, List<String>> body = Map.of("user_ids", List.of(sendbirdUserId));
        // HttpEntity<Map<String, List<String>>> entity = new HttpEntity<>(body, createApiHeaders());
        logger.info("[MOCK] removeUserFromChannel: Attempting to remove user {} from Sendbird channel {}", sendbirdUserId, channelUrl);
        // try {
        //     restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        //     logger.info("[MOCK] User {} (would have) left Sendbird channel {}.", sendbirdUserId, channelUrl);
        // } catch (Exception e) {
        //     logger.error("[MOCK] Error removing user {} from Sendbird channel {}: {}", sendbirdUserId, channelUrl, e.getMessage());
        // }
    }

    public String generateUserSessionToken(String platformUserId) {
        String sendbirdUserId = "neolearn_user_" + platformUserId;
        logger.info("[MOCK] generateUserSessionToken: Generating for user {} (Sendbird ID: {})", platformUserId, sendbirdUserId);
        return "MOCKED_SENDBIRD_SESSION_TOKEN_FOR_" + sendbirdUserId;
    }
}