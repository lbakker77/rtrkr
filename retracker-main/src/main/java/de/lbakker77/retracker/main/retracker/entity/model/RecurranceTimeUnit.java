package de.lbakker77.retracker.main.retracker.entity.model;

import java.time.temporal.ChronoUnit;

public enum RecurranceTimeUnit {
    DAY,
    WEEK,
    MONTH,
    YEAR;


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
