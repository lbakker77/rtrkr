package de.lbakker77.retracker.core.usecase.dtos;

import de.lbakker77.retracker.core.domain.RecurrenceTimeUnit;

public record RecurrenceConfigDto(int recurrenceInterval, RecurrenceTimeUnit recurrenceTimeUnit) {

}
