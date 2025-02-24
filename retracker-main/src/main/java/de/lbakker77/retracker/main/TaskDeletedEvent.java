package de.lbakker77.retracker.main;

import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.Task;

import java.time.LocalDate;
import java.util.UUID;

public record TaskDeletedEvent(RetrackerList list, Task deletedTask, LocalDate formerDueDate, UUID userId) {
}
