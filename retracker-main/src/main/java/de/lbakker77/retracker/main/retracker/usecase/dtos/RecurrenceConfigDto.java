package de.lbakker77.retracker.main.retracker.usecase.dtos;

import de.lbakker77.retracker.main.retracker.entity.model.RecurranceTimeUnit;

public record RecurrenceConfigDto(int recurrenceInterval, RecurranceTimeUnit recurrenceTimeUnit) {

}
