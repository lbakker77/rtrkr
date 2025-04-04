package de.lbakker77.retracker.main.usecase.dtos;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record RetrackerTaskDto(UUID id, UUID listId, String name, ZonedDateTime dueDate, ZonedDateTime lastEntryDate, TaskCategoryDto category, RecurrenceConfigDto recurrenceConfig, List<EntryHistoryDto> history ) {
}
