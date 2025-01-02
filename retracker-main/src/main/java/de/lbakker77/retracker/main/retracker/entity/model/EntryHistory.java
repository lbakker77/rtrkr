package de.lbakker77.retracker.main.retracker.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Embeddable
public record EntryHistory(
        @Column(nullable = false) ZonedDateTime completionDate,
        @Column() ZonedDateTime lastDueDate,
        @Column(nullable = false) int postponedDays) {
}
