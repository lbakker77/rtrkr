package de.lbakker77.retracker.core.usecase.dtos;

import de.lbakker77.retracker.core.domain.ShareStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ShareConfigDto(UUID userId, String email, String firstName, String lastName, ShareStatus status, ZonedDateTime sharedAt, boolean isOwner) {
}
