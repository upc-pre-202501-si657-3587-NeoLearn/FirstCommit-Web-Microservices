package com.neolearn.roadmaps_service.roadmaps.application.internal.commandservices;

import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapCommandService;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapRepository;
import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.AddCourseToRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.CreateRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.DeleteRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.RemoveCourseFromRoadmapCommand;
import com.neolearn.roadmaps_service.roadmaps.domain.model.entities.CourseReference;
import com.neolearn.roadmaps_service.roadmaps.domain.model.exceptions.RoadmapNotFoundException; // Nueva excepción
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.CourseId;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoadmapCommandServiceImpl implements RoadmapCommandService {

    private final RoadmapRepository roadmapRepository;
    // Podríamos inyectar aquí un cliente para validar Cursos (ACL) en el futuro

    public RoadmapCommandServiceImpl(RoadmapRepository roadmapRepository) {
        this.roadmapRepository = roadmapRepository;
    }

    @Transactional
    public RoadmapId handle(CreateRoadmapCommand command) {
        if (roadmapRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("A roadmap with the name '" + command.name() + "' already exists.");
        }
        var roadmap = new Roadmap(command.name(), command.description());
        roadmapRepository.save(roadmap);
        return roadmap.getId();
    }

    @Transactional
    public void handle(AddCourseToRoadmapCommand command) {
        var roadmapId = RoadmapId.from(command.roadmapId());
        var roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new RoadmapNotFoundException(roadmapId));

        // Aquí podríamos llamar al ACL para validar que el curso existe
        // coursesACL.validateCourseExists(command.courseId());

        var courseId = CourseId.from(command.courseId());
        var courseReference = new CourseReference(courseId, command.sequence());

        roadmap.addCourse(courseReference);
        roadmapRepository.save(roadmap);
    }

    @Transactional
    public void handle(RemoveCourseFromRoadmapCommand command) {
        var roadmapId = RoadmapId.from(command.roadmapId());
        var roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new RoadmapNotFoundException(roadmapId));

        var courseId = CourseId.from(command.courseId());

        roadmap.removeCourse(courseId);
        roadmapRepository.save(roadmap);
    }

    @Transactional
    public void handle(DeleteRoadmapCommand command) {
        var roadmapId = RoadmapId.from(command.roadmapId());
        if (!roadmapRepository.findById(roadmapId).isPresent()) {
            throw new RoadmapNotFoundException(roadmapId);
        }
        roadmapRepository.deleteById(roadmapId);
    }
}