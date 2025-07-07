package com.neolearn.memberships_service.membership.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class DateRange {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public DateRange() {
    }

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange createOpenRange(LocalDateTime startDate) {
        return new DateRange(startDate, null);
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && (endDate == null || now.isBefore(endDate));
    }

    public boolean hasEnded() {
        return endDate != null && LocalDateTime.now().isAfter(endDate);
    }

    public boolean contains(LocalDateTime dateTime) {
        return !dateTime.isBefore(startDate) && (endDate == null || !dateTime.isAfter(endDate));
    }
} 