package de.lbakker77.retracker.main;

import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.Task;

import java.time.LocalDate;
import java.util.UUID;

public record TaskChangedEvent(RetrackerList list, Task task, LocalDate formerDueDate, UUID userId) {}
