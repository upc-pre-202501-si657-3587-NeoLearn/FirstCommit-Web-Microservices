package com.neolearn.courses_service.interfaces.rest.transform;

import com.neolearn.courses_service.domain.model.commands.UpdateCourseCommand;
import com.neolearn.courses_service.interfaces.rest.resources.UpdateCourseResource;

public class UpdateCourseCommandFromResourceAssembler {

    public static UpdateCourseCommand toCommandFromResource(UpdateCourseResource resource) {
        return new UpdateCourseCommand(
                resource.courseId(),
                resource.instructorId(),
                resource.title(),
                resource.description(),
                resource.category(),
                resource.theoryContent(),
                resource.quizContent(),
                resource.codingContent()
        );
    }
}