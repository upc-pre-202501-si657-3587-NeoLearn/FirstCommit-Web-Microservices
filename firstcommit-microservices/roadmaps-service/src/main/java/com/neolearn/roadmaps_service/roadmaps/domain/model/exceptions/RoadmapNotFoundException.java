package com.neolearn.roadmaps_service.roadmaps.domain.model.exceptions;

import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoadmapNotFoundException extends RuntimeException {
    public RoadmapNotFoundException(RoadmapId roadmapId) {
        super("Roadmap with ID " + roadmapId.toString() + " not found.");
    }

    public RoadmapNotFoundException(String name) {
        super("Roadmap with name '" + name + "' not found.");
    }
}