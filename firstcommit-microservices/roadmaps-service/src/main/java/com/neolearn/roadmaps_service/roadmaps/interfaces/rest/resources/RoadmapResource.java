package com.neolearn.roadmaps_service.roadmaps.interfaces.rest.resources;

import java.util.List;

public record RoadmapResource(String id, String name, String description, List<CourseReferenceResource> courses) {
}