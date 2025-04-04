package de.lbakker77.retracker.main.usecase.dtos;

import java.util.UUID;

public record RetrackerListDto(UUID id, String name, boolean shared, long dueCount, boolean defaultList, UUID ownerId, boolean isInvitation, String icon, boolean isOwner) {
}
