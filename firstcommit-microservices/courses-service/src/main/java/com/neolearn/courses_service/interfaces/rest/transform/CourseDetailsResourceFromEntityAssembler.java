package com.neolearn.courses_service.interfaces.rest.transform;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.interfaces.rest.resources.CourseDetailsResource;
import com.neolearn.courses_service.interfaces.rest.resources.CourseContentResource;

public class CourseDetailsResourceFromEntityAssembler {

    public static CourseDetailsResource toResourceFromEntity(Course course, Integer progress) {
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
}