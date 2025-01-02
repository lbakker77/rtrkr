package de.lbakker77.retracker.main.retracker.entity.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Embeddable
public record RecurrenceConfig(int recurrenceInterval, @Enumerated RecurranceTimeUnit recurrenceTimeUnit) {

    public ZonedDateTime calcRecurrenceDate(ZonedDateTime completionDate) {
        return completionDate.plus(recurrenceInterval, recurrenceTimeUnit.toChronoUnit()) ;
    }
}
