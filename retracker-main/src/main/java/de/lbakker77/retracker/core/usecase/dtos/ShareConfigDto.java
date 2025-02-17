package de.lbakker77.retracker.main.core.usecase.dtos;

import de.lbakker77.retracker.main.core.domain.ShareStatus;
import jakarta.persistence.Column;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ShareConfigDto(UUID userId, String email, String firstName, String lastName, ShareStatus status, ZonedDateTime sharedAt, boolean isOwner) {
}
