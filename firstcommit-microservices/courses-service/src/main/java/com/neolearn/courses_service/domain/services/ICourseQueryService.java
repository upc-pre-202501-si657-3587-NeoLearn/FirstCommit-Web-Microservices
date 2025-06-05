package com.neolearn.courses_service.domain.services;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.queries.*;
import com.neolearn.courses_service.domain.model.valueobjects.CourseContent;
import com.neolearn.courses_service.interfaces.rest.resources.CourseDetailsResource;


import java.util.List;
import java.util.Optional;

public interface ICourseQueryService {
    List<Course> handle(GetAllCoursesQuery query);
    Optional<Course> handle(GetCourseByIdQuery query);
    List<Course> handle(GetEnrolledCoursesQuery query);
    CourseDetailsResource handle(GetCourseDetailsQuery query); // DTO especializado

    Optional<Integer> handle(GetUserProgressQuery query);
}
