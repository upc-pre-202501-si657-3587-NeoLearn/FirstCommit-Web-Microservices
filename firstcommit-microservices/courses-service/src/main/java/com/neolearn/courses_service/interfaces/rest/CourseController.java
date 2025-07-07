package com.neolearn.courses_service.interfaces.rest;

import com.neolearn.courses_service.domain.services.ICourseCommandService;
import com.neolearn.courses_service.domain.services.ICourseQueryService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Courses", description = "Course Management Endpoints")
public class CourseController {

    private final ICourseCommandService courseCommandService;
    private final ICourseQueryService courseQueryService;

    // IDs temporales para simular un usuario autenticado.
    // En una implementación real, esto se obtendría del token de seguridad.
    private static final String TEMP_INSTRUCTOR_ID = "auth-instructor-id-123";
    private static final String TEMP_USER_ID = "auth-user-id-456";

    public CourseController(ICourseCommandService courseCommandService, ICourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;
    }

    // ============================== ADMIN ENDPOINTS ============================== //

    @Operation(summary = "Create a new course")
    @PostMapping("/admins/courses")
    public ResponseEntity<CourseResource> createCourse(@RequestBody CreateCourseResource resource) {
        var command = CourseCommandFromResourceAssembler.toCommandFromResource(resource, TEMP_INSTRUCTOR_ID);
        var createdCourse = courseCommandService.handle(command);

        // Construir la URI del nuevo recurso creado
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{courseId}")
                .buildAndExpand(createdCourse.getId())
                .toUri();

        var courseResource = CourseResourceFromEntityAssembler.toResourceFromEntity(createdCourse);
        return ResponseEntity.created(location).body(courseResource);
    }

    @Operation(summary = "Update a course's details")
    @PutMapping("/admins/courses/{courseId}")
    public ResponseEntity<CourseResource> updateCourse(@PathVariable Long courseId, @RequestBody UpdateCourseResource resource) {
        var command = UpdateCourseCommandFromResourceAssembler.toCommandFromResource(courseId, TEMP_INSTRUCTOR_ID, resource);

        // El servicio devuelve un Optional<Course>
        return courseCommandService.handle(command)
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Publish a course")
    @PostMapping("/admins/courses/{courseId}/publish")
    public ResponseEntity<Void> publishCourse(@PathVariable Long courseId) {
        var command = new PublishCourseCommand(courseId, TEMP_INSTRUCTOR_ID);
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a course")
    @DeleteMapping("/admins/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        var command = new DeleteCourseCommand(courseId, TEMP_INSTRUCTOR_ID);
        courseCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all courses for admin view (published and unpublished)")
    @GetMapping("/admins/courses")
    public ResponseEntity<List<CourseResource>> getAllCoursesForAdmin() {
        var query = new GetAllCoursesQuery(false); // onlyPublished = false
        var courses = courseQueryService.handle(query);
        var resources = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .toList(); // .toList() es más moderno que .collect(Collectors.toList())
        return ResponseEntity.ok(resources);
    }


    // ============================== USER ENDPOINTS ============================== //

    @Operation(summary = "Enroll in a course")
    @PostMapping("/users/courses/{courseId}/enroll")
    public ResponseEntity<Void> enrollInCourse(@PathVariable Long courseId) {
        var command = new EnrollCourseCommand(courseId, TEMP_USER_ID);
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Rate a course")
    @PostMapping("/users/courses/{courseId}/rating")
    public ResponseEntity<Void> rateCourse(@PathVariable Long courseId, @RequestBody RateCourseResource resource) {
        var command = new RateCourseCommand(courseId, TEMP_USER_ID, resource.rating());
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update user's progress on a course")
    @PostMapping("/users/courses/{courseId}/progress")
    public ResponseEntity<Void> updateProgress(@PathVariable Long courseId, @RequestBody UpdateProgressResource resource) {
        // Usamos nuestro nuevo UpdateProgressCommand
        var command = new UpdateProgressCommand(TEMP_USER_ID, courseId, resource.lastContentCompletedId(), resource.progressPercentage());
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all courses a user is enrolled in")
    @GetMapping("/users/courses/enrolled")
    public ResponseEntity<List<CourseResource>> getEnrolledCourses() {
        var query = new GetEnrolledCoursesQuery(TEMP_USER_ID);
        var courses = courseQueryService.handle(query);
        var resources = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    // ============================== PUBLIC ENDPOINTS ============================== //

    @Operation(summary = "Get all published courses for the public")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResource>> getAllPublishedCourses() {
        var query = new GetAllCoursesQuery(true); // onlyPublished = true
        var courses = courseQueryService.handle(query);
        var resources = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get public details of a specific course")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseDetailsResource> getCourseDetails(@PathVariable Long courseId) {
        // El QueryService ya devuelve el Resource directamente, según tu diseño.
        var query = new GetCourseDetailsQuery(courseId, TEMP_USER_ID);
        var courseDetails = courseQueryService.handle(query);

        // Manejo de caso no encontrado (si el servicio devolviera Optional o lanzara excepción)
        if (courseDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courseDetails);
    }
}