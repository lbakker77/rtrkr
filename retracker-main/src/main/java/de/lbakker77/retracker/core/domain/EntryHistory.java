package de.lbakker77.retracker.main.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Embeddable
public record EntryHistory (
        @Column(nullable = false) LocalDate completionDate,
        @Column() LocalDate dueDate,
        @Column(nullable = false) int postponedDays,
        @Column() LocalDate lastCompletionDate) implements Comparable<EntryHistory> {

    public int compareTo(EntryHistory other) {
        return -1 * completionDate.compareTo(other.completionDate());
    }

    public int getOverdueDays() {
        if (dueDate == null) {
            return 0;
        }
        return dueDate.isBefore(completionDate)? (int) dueDate.until(completionDate, ChronoUnit.DAYS) : 0;
    }

    public int getPlannedDays() {
        if (dueDate == null || lastCompletionDate == null) {
            return 0;
        }
        return (int) lastCompletionDate.until(dueDate, ChronoUnit.DAYS) - postponedDays;
    }


}
