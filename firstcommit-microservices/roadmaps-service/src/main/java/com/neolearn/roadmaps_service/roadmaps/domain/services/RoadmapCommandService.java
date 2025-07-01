package com.neolearn.roadmaps_service.roadmaps.domain.services;

import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.AddCourseToRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.CreateRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.DeleteRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.RemoveCourseFromRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;

// Define los métodos para manejar cada comando.
public interface RoadmapCommandService {
    RoadmapId handle(CreateRoadmapCommand command);
    void handle(AddCourseToRoadmapCommand command);
    void handle(RemoveCourseFromRoadmapCommand command);
    void handle(DeleteRoadmapCommand command);
}