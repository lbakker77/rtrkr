package de.lbakker77.retracker.main.retracker.usecase.dtos;

import de.lbakker77.retracker.main.retracker.entity.model.EntryHistory;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record RetrackerEntryDto(UUID id, String name, ZonedDateTime dueDate, ZonedDateTime lastEntryDate, UserCategoryDto userCategory, RecurrenceConfigDto recurrenceConfig, List<EntryHistory> history ) {
}
