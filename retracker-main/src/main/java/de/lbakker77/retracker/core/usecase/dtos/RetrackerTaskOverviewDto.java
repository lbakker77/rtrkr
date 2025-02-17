package de.lbakker77.retracker.core.usecase.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

public record RetrackerTaskOverviewDto(UUID id, UUID listId, String name, ZonedDateTime dueDate, ZonedDateTime lastEntryDate, UserCategoryDto userCategory, RecurrenceConfigDto recurrenceConfig) {

}
