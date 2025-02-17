package de.lbakker77.retracker.core.domain;

import java.time.temporal.ChronoUnit;

public enum RecurrenceTimeUnit {
    DAY,
    WEEK,
    MONTH,
    YEAR, DAYS;


    public ChronoUnit toChronoUnit() {
        return switch (this) {
            case DAY -> ChronoUnit.DAYS;
            case WEEK -> ChronoUnit.WEEKS;
            case MONTH -> ChronoUnit.MONTHS;
            case YEAR -> ChronoUnit.YEARS;
            default -> ChronoUnit.DAYS;
        };
    }
}
