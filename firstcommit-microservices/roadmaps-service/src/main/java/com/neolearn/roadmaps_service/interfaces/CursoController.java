package com.neolearn.roadmaps_service.interfaces;

import com.neolearn.roadmaps_service.application.CursoService;
import com.neolearn.roadmaps_service.domain.model.Curso;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> getAll() {
        return cursoService.findAll();
    }

    @GetMapping("/{id}")
    public Curso getById(@PathVariable Long id) {
        return cursoService.findById(id).orElseThrow();
    }

    @PostMapping
    public Curso create(@RequestBody Curso curso) {
        return cursoService.save(curso);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cursoService.delete(id);
    }
}
