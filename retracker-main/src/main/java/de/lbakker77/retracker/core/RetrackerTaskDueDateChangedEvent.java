package de.lbakker77.retracker.core;

import java.time.LocalDate;
import java.util.UUID;

public record RetrackerTaskDueDateChangedEvent(UUID entryId, LocalDate formerDueDate) {}
