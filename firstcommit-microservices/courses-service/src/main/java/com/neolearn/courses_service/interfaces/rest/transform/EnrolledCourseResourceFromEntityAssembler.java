package com.neolearn.courses_service.interfaces.rest.transform;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.interfaces.rest.resources.CourseResource;

public class EnrolledCourseResourceFromEntityAssembler {

    public static CourseResource toResourceFromEntity(Course course, Integer progress) {
        return new CourseResource(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getInstructorId(),
                course.isPublished(),
                course.getAverageRating()

        );
    }
}