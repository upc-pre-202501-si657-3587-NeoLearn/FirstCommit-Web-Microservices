package com.neolearn.roadmaps_service.roadmaps.domain.services;

import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.*;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;

// Define los métodos para manejar cada comando.
public interface RoadmapCommandService {
    RoadmapId handle(CreateRoadmapCommand command);
    void handle(AddCourseToRoadmapCommand command);
    void handle(RemoveCourseFromRoadmapCommand command);
    void handle(DeleteRoadmapCommand command);
    void handle(UpdateRoadmapDetailsCommand command);
    void handle(RegisterAvailableCourseCommand command);
}