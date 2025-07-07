// Ubicación: src/main/java/com/neolearn/courses_service/interfaces/rest/transform/UpdateCourseCommandFromResourceAssembler.java
package com.neolearn.courses_service.interfaces.rest.transform;

import com.neolearn.courses_service.domain.model.commands.UpdateCourseCommand;
import com.neolearn.courses_service.interfaces.rest.resources.UpdateCourseResource;

public class UpdateCourseCommandFromResourceAssembler {

    /**
     * Ensambla un UpdateCourseCommand a partir de los datos de la petición.
     *
     * @param courseId     El ID del curso, proveniente de la URL (@PathVariable).
     * @param instructorId El ID del instructor, gestionado por el backend.
     * @param resource     El cuerpo de la petición (@RequestBody) con los datos a actualizar.
     * @return El comando listo para ser procesado.
     */
    public static UpdateCourseCommand toCommandFromResource(Long courseId, String instructorId, UpdateCourseResource resource) {
        // El orden de los argumentos aquí DEBE coincidir con el constructor del record UpdateCourseCommand
        return new UpdateCourseCommand(
                courseId,
                instructorId,
                resource.title(),
                resource.description(),
                resource.category(),
                resource.theoryContent(),
                resource.quizContent(),
                resource.codingContent()
        );
    }
}