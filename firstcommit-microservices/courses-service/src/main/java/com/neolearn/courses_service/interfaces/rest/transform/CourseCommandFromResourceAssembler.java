package com.neolearn.courses_service.interfaces.rest.transform;

import com.neolearn.courses_service.domain.model.commands.CreateCourseCommand;
import com.neolearn.courses_service.interfaces.rest.resources.CreateCourseResource;

public class CourseCommandFromResourceAssembler {

    public static CreateCourseCommand toCommandFromResource(CreateCourseResource resource) {
        return new CreateCourseCommand(
                resource.title(),
                resource.description(),
                resource.category(),
                resource.instructorId(),
                resource.theoryContent(),
                resource.quizContent(),
                resource.codingContent()
        );
    }
}