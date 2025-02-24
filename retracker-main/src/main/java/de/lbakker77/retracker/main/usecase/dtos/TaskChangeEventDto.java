package de.lbakker77.retracker.main.usecase.dtos;

import java.util.UUID;

public record TaskChangeEventDto(UUID taskId, ChangeType changeType, boolean dueCountChanged, UUID userId) {
}
