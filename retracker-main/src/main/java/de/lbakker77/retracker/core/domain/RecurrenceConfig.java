package de.lbakker77.retracker.core.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Embeddable
public record RecurrenceConfig(int recurrenceInterval, @Enumerated RecurrenceTimeUnit recurrenceTimeUnit) {

    public LocalDate calcRecurrenceDate(LocalDate completionDate) {
        return completionDate.plus(recurrenceInterval, recurrenceTimeUnit.toChronoUnit()) ;
    }
}
