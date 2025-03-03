package de.lbakker77.retracker.main.usecase.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

public record RetrackerTaskOverviewDto(UUID id, UUID listId, String name, ZonedDateTime dueDate, ZonedDateTime lastEntryDate, TaskCategoryDto category, RecurrenceConfigDto recurrenceConfig) {

}
