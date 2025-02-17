package de.lbakker77.retracker.main.core.usecase.dtos;

import de.lbakker77.retracker.main.core.domain.RecurrenceTimeUnit;

public record RecurrenceConfigDto(int recurrenceInterval, RecurrenceTimeUnit recurrenceTimeUnit) {

}
