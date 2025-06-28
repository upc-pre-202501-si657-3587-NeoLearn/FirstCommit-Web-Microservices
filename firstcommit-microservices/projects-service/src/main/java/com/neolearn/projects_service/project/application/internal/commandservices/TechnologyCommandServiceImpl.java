package com.neolearn.projects_service.project.application.internal.commandservices;

import com.neolearn.projects_service.project.domain.model.commands.CreateTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.DeleteTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.commands.UpdateTechnologyCommand;
import com.neolearn.projects_service.project.domain.model.entities.Technology;
import com.neolearn.projects_service.project.domain.services.TechnologyCommandService;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.ProjectTechnologyRepository;
import com.neolearn.projects_service.project.infrastructure.persistence.jpa.repositories.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TechnologyCommandServiceImpl implements TechnologyCommandService {

    private final TechnologyRepository technologyRepository;
    private final ProjectTechnologyRepository projectTechnologyRepository;

    public TechnologyCommandServiceImpl(TechnologyRepository technologyRepository, 
                                      ProjectTechnologyRepository projectTechnologyRepository) {
        this.technologyRepository = technologyRepository;
        this.projectTechnologyRepository = projectTechnologyRepository;
    }

    @Override
    @Transactional
    public Optional<Technology> handle(CreateTechnologyCommand command) {
        if (technologyRepository.existsByNombre(command.nombre())) {
            throw new IllegalStateException("Ya existe una tecnología con este nombre");
        }

        Technology technology = new Technology(command.nombre());
        return Optional.of(technologyRepository.save(technology));
    }

    @Override
    @Transactional
    public Optional<Technology> handle(UpdateTechnologyCommand command) {
        return technologyRepository.findById(command.idTecnologia())
                .map(technology -> {
                    if (!technology.getNombre().equals(command.nombre()) && 
                        technologyRepository.existsByNombre(command.nombre())) {
                        throw new IllegalStateException("Ya existe una tecnología con este nombre");
                    }
                    
                    technology.updateNombre(command.nombre());
                    return technologyRepository.save(technology);
                });
    }

    @Override
    @Transactional
    public boolean handle(DeleteTechnologyCommand command) {
        if (!technologyRepository.existsById(command.idTecnologia())) {
            return false;
        }

        if (projectTechnologyRepository.existsByIdIdTecnologia(command.idTecnologia())) {
            throw new IllegalStateException("No se puede eliminar la tecnología porque está asociada a uno o más proyectos");
        }

        technologyRepository.deleteById(command.idTecnologia());
        return true;
    }
}