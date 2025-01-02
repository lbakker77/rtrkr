package de.lbakker77.retracker.main.retracker.usecase.dtos;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

public record RetrackerOverviewEntryDto(UUID id, String name, ZonedDateTime dueDate, ZonedDateTime lastEntryDate, UserCategoryDto userCategory, RecurrenceConfigDto recurrenceConfig) {

}
