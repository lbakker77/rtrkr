package de.lbakker77.retracker.main.usecase.dtos;

import de.lbakker77.retracker.main.domain.RecurrenceTimeUnit;

public record RecurrenceConfigDto(int recurrenceInterval, RecurrenceTimeUnit recurrenceTimeUnit) {

}
