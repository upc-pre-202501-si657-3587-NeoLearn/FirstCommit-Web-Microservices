package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.SendMessageCommand;
import com.neolearn.projects_service.project.domain.model.queries.GetProjectMessagesQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.MessageResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.SendMessageResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.MessageResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Messages", description = "API de Gestión de Mensajes en Proyectos")
public class MessageController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    public MessageController(ProjectCommandService projectCommandService, ProjectQueryService projectQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
    }

    @PostMapping("/{projectId}/messages")
    @Operation(summary = "Enviar un mensaje", description = "Envía un mensaje en el proyecto")
    public ResponseEntity<MessageResource> sendMessage(
            @PathVariable Long projectId,
            @RequestBody SendMessageResource resource,
            @RequestHeader("X-User-Id") Long currentUserId) {

        var command = new SendMessageCommand(
                projectId,
                currentUserId,
                resource.getContenido()
        );

        var message = projectCommandService.handle(command);
        if (message.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var messageResource = MessageResourceFromEntityAssembler.toResourceFromEntity(message.get());
        return new ResponseEntity<>(messageResource, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}/messages")
    @Operation(summary = "Obtener mensajes de un proyecto", description = "Retorna la lista de mensajes de un proyecto")
    public ResponseEntity<List<MessageResource>> getProjectMessages(@PathVariable Long projectId) {
        var query = new GetProjectMessagesQuery(projectId);
        var messages = projectQueryService.handle(query);

        var messageResources = messages.stream()
                .map(MessageResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageResources);
    }
} 