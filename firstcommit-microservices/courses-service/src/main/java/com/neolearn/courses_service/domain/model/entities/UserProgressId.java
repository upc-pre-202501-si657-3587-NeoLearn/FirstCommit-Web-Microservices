// Ubicación: src/main/java/com/neolearn/courses_service/domain/model/aggregates/UserProgressId.java
package com.neolearn.courses_service.domain.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class UserProgressId implements Serializable {
    private Long course; // El nombre debe coincidir con el campo en UserProgress
    private String userId;

    public UserProgressId() {}

    public UserProgressId(Long course, String userId) {
        this.course = course;
        this.userId = userId;
    }

    // Es crucial implementar equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProgressId that = (UserProgressId) o;
        return Objects.equals(course, that.course) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, userId);
    }
}