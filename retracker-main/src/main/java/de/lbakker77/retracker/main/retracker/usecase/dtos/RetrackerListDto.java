package de.lbakker77.retracker.main.retracker.usecase.dtos;

import java.util.UUID;

public record RetrackerListDto(UUID id, String name, boolean shared, long dueCount) {
}
