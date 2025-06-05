package com.neolearn.courses_service.application.internal.queryservices;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.queries.*;
import com.neolearn.courses_service.domain.services.ICourseQueryService;
import com.neolearn.courses_service.infrastructure.persistence.jpa.repositories.CourseRepository;
import com.neolearn.courses_service.interfaces.rest.resources.CourseDetailsResource;
import com.neolearn.courses_service.interfaces.rest.resources.CourseContentResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CourseQueryService implements ICourseQueryService {

    private final CourseRepository courseRepository;

    public CourseQueryService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> handle(GetAllCoursesQuery query) {
        if (query.onlyPublished()) {
            return courseRepository.findByPublishedTrue();
        }
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> handle(GetCourseByIdQuery query) {
        if (query.courseId() == null || query.courseId() <= 0) {
            throw new IllegalArgumentException("Invalid course ID");
        }
        return courseRepository.findById(query.courseId());
    }

    @Override
    public List<Course> handle(GetEnrolledCoursesQuery query) {
        if (query.userId() == null || query.userId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return courseRepository.findCoursesByEnrolledUser(query.userId());
    }

    @Override
    public CourseDetailsResource handle(GetCourseDetailsQuery query) {
        if (query.courseId() == null || query.courseId() <= 0) {
            throw new IllegalArgumentException("Invalid course ID");
        }

        Course course = courseRepository.findById(query.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Integer progress = null;
        if (query.userId() != null && !query.userId().isEmpty()) {
            progress = course.getUserProgress(query.userId()).orElse(null);
        }

        return new CourseDetailsResource(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getInstructorId(),
                course.getImageUrl(),
                course.isPublished(),
                course.getAverageRating(),
                progress,
                new CourseContentResource(
                        course.getContent().getTheoryContent(),
                        course.getContent().getQuizContent(),
                        course.getContent().getCodingContent()
                )
        );
    }

    @Override
    public Optional<Integer> handle(GetUserProgressQuery query) {
        if (query.courseId() == null || query.courseId() <= 0) {
            throw new IllegalArgumentException("Invalid course ID");
        }
        if (query.userId() == null || query.userId().isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return courseRepository.findById(query.courseId())
                .flatMap(course -> course.getUserProgress(query.userId()));
    }
}