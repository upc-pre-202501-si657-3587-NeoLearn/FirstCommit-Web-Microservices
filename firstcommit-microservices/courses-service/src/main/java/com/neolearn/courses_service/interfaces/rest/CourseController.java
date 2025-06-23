package com.neolearn.courses_service.interfaces.rest;

import com.neolearn.courses_service.application.internal.commandservices.CourseCommandService;
import com.neolearn.courses_service.application.internal.queryservices.CourseQueryService;
import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.commands.CompleteContentCommand;
import com.neolearn.courses_service.domain.model.commands.*;
import com.neolearn.courses_service.domain.model.queries.*;
import com.neolearn.courses_service.interfaces.rest.resources.*;
import com.neolearn.courses_service.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Courses", description = "Course Management Endpoints")
public class CourseController {

    private final CourseCommandService courseCommandService;
    private final CourseQueryService courseQueryService;


    // IDs temporales para desarrollo. ¡No usar en producción!
    // Puedes cambiarlos por cualquier valor que te sirva para probar.
    private static final String TEMP_INSTRUCTOR_ID = "instructor-12345-abcde";
    private static final String TEMP_USER_ID = "user-67890-fghij";

    public CourseController(
            CourseCommandService courseCommandService,
            CourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;
    }

    // ============================== ADMIN ENDPOINTS ============================== //


    @Operation(summary = "Create a new course")
    @PostMapping("/admins/courses")
    public ResponseEntity<CourseResource> createCourse(
            @RequestBody CreateCourseResource courseResource) {

        // El assembler debe ser modificado para aceptar el instructorId como argumento
        var command = CourseCommandFromResourceAssembler.toCommandFromResource(courseResource, TEMP_INSTRUCTOR_ID);

        Course course = courseCommandService.handle(command);
        var response = CourseResourceFromEntityAssembler.toResourceFromEntity(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all courses (published and unpublished) for admin view ")
    @GetMapping("/admins/courses")
    public ResponseEntity<List<CourseResource>> getAllCoursesForAdmin() {
        // Creamos la consulta, pidiendo TODOS los cursos (onlyPublished = false)
        var query = new GetAllCoursesQuery(false);
        var courses = courseQueryService.handle(query);

        // Transformamos la lista de entidades a una lista de recursos
        var response = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Update a course [CORRECTED & SECURED]")
    @PutMapping("/admins/courses/{courseId}")
    public ResponseEntity<CourseResource> updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseResource updateResource) { // Usamos el Resource corregido

        // 1. Ensamblar el comando con el flujo de datos correcto.
        var command = UpdateCourseCommandFromResourceAssembler.toCommandFromResource(
                courseId,
                TEMP_INSTRUCTOR_ID,
                updateResource
        );

        // 2. Enviar el comando al servicio de aplicación.
        var updatedCourseOptional = courseCommandService.handle(command);

        // 3. Devolver la respuesta. Si el Optional está vacío (curso no encontrado), devuelve 404.
        return updatedCourseOptional
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Publish a course [SECURITY DISABLED]")
    @PostMapping("/admins/courses/{courseId}/publish")
    public ResponseEntity<Void> publishCourse(@PathVariable Long courseId) {
        var command = new PublishCourseCommand(courseId, TEMP_INSTRUCTOR_ID);
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a course [SECURITY DISABLED]")
    @DeleteMapping("/admins/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        // 1. Crear el comando de borrado usando el ID de la ruta y el ID temporal.
        var command = new DeleteCourseCommand(courseId, TEMP_INSTRUCTOR_ID);

        // 2. Enviar el comando al servicio para que lo maneje.
        courseCommandService.handle(command);

        // 3. Devolver una respuesta HTTP 204 No Content, que indica éxito sin cuerpo de respuesta.
        return ResponseEntity.noContent().build();
    }

    // ============================== USER ENDPOINTS ============================== //

    @Operation(summary = "Enroll in a course [SECURITY DISABLED]")
    @PostMapping("/users/courses/{courseId}/enroll")
    public ResponseEntity<Void> enrollInCourse(@PathVariable Long courseId) {
        var command = new EnrollCourseCommand(courseId, TEMP_USER_ID);
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get enrolled courses [SECURITY DISABLED]")
    @GetMapping("/users/courses/enrolled")
    public ResponseEntity<List<CourseResource>> getEnrolledCourses() {
        var query = new GetEnrolledCoursesQuery(TEMP_USER_ID);
        var courses = courseQueryService.handle(query);

        var response = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Mark a piece of content as completed for a user")
    @PostMapping("/users/courses/{courseId}/content/{contentId}/complete")
    public ResponseEntity<Void> completeContent(
            @PathVariable Long courseId,
            @PathVariable String contentId,
            @RequestHeader("X-User-Id") String userId) { // Asumiendo que usas cabeceras para el ID

        // Creamos un nuevo tipo de comando
        var command = new CompleteContentCommand(userId, courseId, contentId);
        courseCommandService.handle(command);

        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Rate a course [SECURITY DISABLED]")
    @PostMapping("/users/courses/{courseId}/rating")
    public ResponseEntity<Void> rateCourse(
            @PathVariable Long courseId,
            @RequestBody RateCourseResource ratingResource) {
        var command = new RateCourseCommand(
                courseId,
                TEMP_USER_ID,
                ratingResource.rating());

        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    // ============================== PUBLIC ENDPOINTS ============================== //

    @Operation(summary = "Get all published courses")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResource>> getAllPublishedCourses() {
        var query = new GetAllCoursesQuery(true); // onlyPublished = true
        var courses = courseQueryService.handle(query);

        var response = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get course details")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseDetailsResource> getCourseDetails(@PathVariable Long courseId) {
        // Pasamos el ID de usuario temporal para que la consulta pueda determinar
        // si este "usuario" está inscrito, como si estuviera logueado.
        // Si no quieres simular un usuario logueado, puedes pasar null.
        var query = new GetCourseDetailsQuery(courseId, TEMP_USER_ID);
        var courseDetails = courseQueryService.handle(query);

        return ResponseEntity.ok(courseDetails);
    }
}