package com.neolearn.courses_service.domain.services;

import com.neolearn.courses_service.domain.model.aggregates.Course;
import com.neolearn.courses_service.domain.model.commands.*;

import java.util.Optional;

public interface ICourseCommandService {
    Course handle(CreateCourseCommand command);
    Optional<Course> handle(UpdateCourseCommand command);
    void handle(PublishCourseCommand command);
    void handle(EnrollCourseCommand command);
    void handle(RateCourseCommand command);
    void handle(UpdateProgressCommand command);
    void handle(DeleteCourseCommand command);

}
