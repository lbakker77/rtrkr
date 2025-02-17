package de.lbakker77.retracker.main.core.usecase.dtos;

import java.time.ZonedDateTime;

public record EntryHistoryDto(ZonedDateTime completionDate, ZonedDateTime dueDate, int postponedDays, int plannedDays, int overdueDays) {
}
