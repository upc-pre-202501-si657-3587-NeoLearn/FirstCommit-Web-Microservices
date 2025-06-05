package com.neolearn.courses_service;

import com.neolearn.courses_service.domain.model.valueobjects.Rating;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RatingTest {

    @Test
    void whenValidRating_thenSuccess() {
        Rating rating = new Rating(5, "user-123");
        assertThat(rating.getValue()).isEqualTo(5);
    }

    @Test
    void whenRatingBelowMin_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Rating(0, "user-123")
        );
    }

    @Test
    void whenUserIdIsNull_thenThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new Rating(5, null)
        );
    }
}