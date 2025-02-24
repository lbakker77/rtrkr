package de.lbakker77.retracker.main;

import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.Task;

import java.util.UUID;

public record TaskCreatedEvent(RetrackerList list, Task task, UUID userId) {
}
