package com.neolearn.roadmaps_service.interfaces;

import com.neolearn.roadmaps_service.application.ModuloService;
import com.neolearn.roadmaps_service.domain.model.Curso;
import com.neolearn.roadmaps_service.domain.model.Modulo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modulos")
public class ModuloController {
    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
    }

    @GetMapping("/cursos/{moduloId}")
    public ModuloService.ModuloWithCourses getModuloWithCourses(@PathVariable Long moduloId) {
        return moduloService.getModuloWithCourses(moduloId);
    }

}
