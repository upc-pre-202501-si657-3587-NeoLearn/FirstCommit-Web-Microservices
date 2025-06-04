package com.neolearn.roadmaps_service.application;

import com.neolearn.roadmaps_service.domain.model.Curso;
import com.neolearn.roadmaps_service.domain.model.Modulo;
import com.neolearn.roadmaps_service.domain.repositories.ICursoRepository;
import com.neolearn.roadmaps_service.domain.repositories.IModuloRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuloService {
    private final IModuloRepository moduloRepository;
    private final ICursoRepository cursoRepository;

    public ModuloWithCourses getModuloWithCourses(Long moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId).orElseThrow();
        List<Curso> cursos = cursoRepository.findAllById(modulo.getCourseIds());
        return new ModuloWithCourses(modulo, cursos);
    }

    @Getter
    @AllArgsConstructor
    public static class ModuloWithCourses {
        private final Modulo modulo;
        private final List<Curso> cursos;
    }
}
