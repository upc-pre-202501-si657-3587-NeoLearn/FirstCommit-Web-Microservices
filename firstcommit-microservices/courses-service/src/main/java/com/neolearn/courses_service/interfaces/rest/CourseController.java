package com.neolearn.courses_service.interfaces.rest;

import com.neolearn.courses_service.application.internal.commandservices.CourseCommandService;
import com.neolearn.courses_service.application.internal.queryservices.CourseQueryService;
import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.commands.*;
import com.neolearn.courses_service.domain.model.queries.*;
import com.neolearn.courses_service.interfaces.rest.resources.*;
import com.neolearn.courses_service.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Courses", description = "Course Management Endpoints")
public class CourseController {

    private final CourseCommandService courseCommandService;
    private final CourseQueryService courseQueryService;

    public CourseController(
            CourseCommandService courseCommandService,
            CourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;
    }

    // ============================== ADMIN ENDPOINTS ============================== //

    @Operation(summary = "Create a new course", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/courses")
    public ResponseEntity<CourseResource> createCourse(
            @RequestBody CreateCourseResource courseResource,
            @AuthenticationPrincipal Jwt jwt) {

        String instructorId = jwt.getSubject();
        var command = CourseCommandFromResourceAssembler.toCommandFromResource(
                new CreateCourseResource(
                        courseResource.title(),
                        courseResource.description(),
                        courseResource.category(),
                        instructorId,
                        courseResource.theoryContent(),
                        courseResource.quizContent(),
                        courseResource.codingContent()
                ));

        Course course = courseCommandService.handle(command);
        var response = CourseResourceFromEntityAssembler.toResourceFromEntity(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a course", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/courses/{courseId}")
    public ResponseEntity<CourseResource> updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseResource updateResource,
            @AuthenticationPrincipal Jwt jwt) {

        String instructorId = jwt.getSubject();
        var command = UpdateCourseCommandFromResourceAssembler.toCommandFromResource(
                new UpdateCourseResource(
                        courseId,
                        instructorId,
                        updateResource.title(),
                        updateResource.description(),
                        updateResource.category(),
                        updateResource.theoryContent(),
                        updateResource.quizContent(),
                        updateResource.codingContent()
                ));

        var updatedCourse = courseCommandService.handle(command);

        if (updatedCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var response = CourseResourceFromEntityAssembler.toResourceFromEntity(updatedCourse.get());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Publish a course", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/courses/{courseId}/publish")
    public ResponseEntity<Void> publishCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Jwt jwt) {

        String instructorId = jwt.getSubject();
        var command = new PublishCourseCommand(courseId, instructorId);
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a course", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Jwt jwt) {

        String instructorId = jwt.getSubject();
        // Necesitarías implementar DeleteCourseCommand y su manejo en CourseCommandService
        // var command = new DeleteCourseCommand(courseId, instructorId);
        // courseCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }

    // ============================== USER ENDPOINTS ============================== //

    @Operation(summary = "Enroll in a course", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user/courses/{courseId}/enroll")
    public ResponseEntity<Void> enrollInCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        var command = new EnrollCourseCommand(courseId, userId);
        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get enrolled courses", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/courses/enrolled")
    public ResponseEntity<List<CourseResource>> getEnrolledCourses(
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        var query = new GetEnrolledCoursesQuery(userId);
        var courses = courseQueryService.handle(query);

        var response = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update course progress", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user/courses/{courseId}/progress")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long courseId,
            @RequestBody UpdateProgressResource progressResource,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        var command = new UpdateProgressCommand(
                courseId,
                userId,
                progressResource.progress());

        courseCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Rate a course", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user/courses/{courseId}/rating")
    public ResponseEntity<Void> rateCourse(
            @PathVariable Long courseId,
            @RequestBody RateCourseResource ratingResource,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        var command = new RateCourseCommand(
                courseId,
                userId,
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
    public ResponseEntity<CourseDetailsResource> getCourseDetails(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt != null ? jwt.getSubject() : null;
        var query = new GetCourseDetailsQuery(courseId, userId);
        var courseDetails = courseQueryService.handle(query);

        return ResponseEntity.ok(courseDetails);
    }
}