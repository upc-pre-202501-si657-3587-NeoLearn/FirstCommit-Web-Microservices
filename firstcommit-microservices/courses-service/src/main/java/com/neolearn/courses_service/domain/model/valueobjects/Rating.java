package com.neolearn.courses_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
public class Rating {

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final Integer value;
    private final String userId;

    // Constructor protegido para JPA
    protected Rating() {
        this.value = null;
        this.userId = null;
    }

    public Rating(Integer value, String userId) {
        validateRating(value);
        this.value = value;
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
    }

    private void validateRating(Integer rating) {
        if (rating == null || rating < MIN_RATING || rating > MAX_RATING) {
            throw new IllegalArgumentException(
                    String.format("Rating must be between %d and %d", MIN_RATING, MAX_RATING)
            );
        }
    }

    // Métodos para comparar Ratings
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(value, rating.value) &&
                Objects.equals(userId, rating.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, userId);
    }
}