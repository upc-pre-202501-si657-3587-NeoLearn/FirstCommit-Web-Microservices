package com.neolearn.courses_service.interfaces.rest.transform;

import com.neolearn.courses_service.domain.model.commands.RateCourseCommand;
import com.neolearn.courses_service.interfaces.rest.resources.RateCourseResource;

public class RateCourseCommandFromResourceAssembler {

    public static RateCourseCommand toCommandFromResource(RateCourseResource resource) {
        return new RateCourseCommand(
                resource.courseId(),
                resource.userId(),
                resource.rating()
        );
    }
}