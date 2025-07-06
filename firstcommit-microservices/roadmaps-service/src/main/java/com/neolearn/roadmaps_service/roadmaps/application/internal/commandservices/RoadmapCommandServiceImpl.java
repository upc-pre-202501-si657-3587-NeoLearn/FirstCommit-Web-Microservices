package com.neolearn.roadmaps_service.roadmaps.application.internal.commandservices;

import com.neolearn.roadmaps_service.roadmaps.domain.model.aggregates.Roadmap;
import com.neolearn.roadmaps_service.roadmaps.domain.model.commands.*;
import com.neolearn.roadmaps_service.roadmaps.domain.model.entities.CourseReference;
import com.neolearn.roadmaps_service.roadmaps.domain.model.exceptions.RoadmapNotFoundException;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.CourseId;
import com.neolearn.roadmaps_service.roadmaps.domain.model.valueobjects.RoadmapId;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapCommandService;
import com.neolearn.roadmaps_service.roadmaps.domain.services.RoadmapRepository; // Asegúrate de que este import es el correcto
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoadmapCommandServiceImpl implements RoadmapCommandService {

    private final RoadmapRepository roadmapRepository;

    public RoadmapCommandServiceImpl(RoadmapRepository roadmapRepository) {
        this.roadmapRepository = roadmapRepository;
    }

    @Override
    @Transactional
    public RoadmapId handle(CreateRoadmapCommand command) {
        if (roadmapRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("A roadmap with the name '" + command.name() + "' already exists.");
        }
        var roadmap = new Roadmap(command.name(), command.description());
        roadmapRepository.save(roadmap);
        return roadmap.getId();
    }

    @Override
    @Transactional
    public void handle(AddCourseToRoadmapCommand command) {
        var roadmapId = RoadmapId.from(command.roadmapId());
        var roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new RoadmapNotFoundException(roadmapId));

        var courseId = CourseId.from(command.courseId());
        var courseReference = new CourseReference(courseId, command.sequence());

        roadmap.addCourse(courseReference);
        roadmapRepository.save(roadmap);
    }

    @Override
    @Transactional
    public void handle(RemoveCourseFromRoadmapCommand command) {
        var roadmapId = RoadmapId.from(command.roadmapId());
        var roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new RoadmapNotFoundException(roadmapId));

        var courseId = CourseId.from(command.courseId());

        roadmap.removeCourse(courseId);
        roadmapRepository.save(roadmap);
    }

    @Override
    @Transactional
    public void handle(DeleteRoadmapCommand command) {
        var roadmapId = RoadmapId.from(command.roadmapId());
        if (!roadmapRepository.existsById(roadmapId)) { // Usar existsById es más eficiente
            throw new RoadmapNotFoundException(roadmapId);
        }
        roadmapRepository.deleteById(roadmapId);
    }

    /**
     * Maneja el comando para actualizar los detalles de un Roadmap existente.
     * @param command el comando con el ID del roadmap y los nuevos detalles.
     */
    @Override
    @Transactional
    public void handle(UpdateRoadmapDetailsCommand command) {
        // 1. Buscar el roadmap existente en la base de datos por su ID.
        var roadmap = roadmapRepository.findById(RoadmapId.from(command.roadmapId()))
                .orElseThrow(() -> new RoadmapNotFoundException(RoadmapId.from(command.roadmapId())));

        // 2. Actualizar las propiedades del roadmap con los datos del comando.
        // Es una buena práctica que la propia entidad gestione su actualización.
        roadmap.updateDetails(command.name(), command.description());

        // 3. Guardar el roadmap actualizado en la base de datos.
        roadmapRepository.save(roadmap);
    }
}