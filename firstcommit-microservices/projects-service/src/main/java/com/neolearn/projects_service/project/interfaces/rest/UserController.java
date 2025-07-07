package com.neolearn.projects_service.project.interfaces.rest;

import com.neolearn.projects_service.project.domain.model.commands.CreateUserCommand;
import com.neolearn.projects_service.project.domain.model.entities.User;
import com.neolearn.projects_service.project.domain.model.queries.GetUserByEmailQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetUserByIdQuery;
import com.neolearn.projects_service.project.domain.model.queries.GetUserByUsernameQuery;
import com.neolearn.projects_service.project.domain.services.ProjectCommandService;
import com.neolearn.projects_service.project.domain.services.ProjectQueryService;
import com.neolearn.projects_service.project.interfaces.rest.resources.CreateUserResource;
import com.neolearn.projects_service.project.interfaces.rest.resources.UserResource;
import com.neolearn.projects_service.project.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "API de Gestión de Usuarios")
public class UserController {

    private final ProjectQueryService projectQueryService;
    private final ProjectCommandService projectCommandService;

    public UserController(ProjectQueryService projectQueryService, ProjectCommandService projectCommandService) {
        this.projectQueryService = projectQueryService;
        this.projectCommandService = projectCommandService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    public ResponseEntity<UserResource> createUser(@RequestBody CreateUserResource resource) {
        var command = new CreateUserCommand(
                resource.getId(),
                resource.getNombreUsuario(),
                resource.getEmail()
        );

        var user = projectCommandService.handle(command);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Obtener un usuario por ID", description = "Retorna un usuario específico por su ID")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var query = new GetUserByIdQuery(userId);
        var user = projectQueryService.handle(query);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener un usuario por email", description = "Retorna un usuario específico por su email")
    public ResponseEntity<UserResource> getUserByEmail(@PathVariable String email) {
        var query = new GetUserByEmailQuery(email);
        var user = projectQueryService.handle(query);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Obtener un usuario por nombre de usuario", description = "Retorna un usuario específico por su nombre de usuario")
    public ResponseEntity<UserResource> getUserByUsername(@PathVariable String username) {
        var query = new GetUserByUsernameQuery(username);
        var user = projectQueryService.handle(query);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }
} 