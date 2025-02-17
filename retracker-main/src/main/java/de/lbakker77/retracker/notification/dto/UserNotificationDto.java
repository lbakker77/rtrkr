package de.lbakker77.retracker.notification.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record UserNotificationDto(UUID id, String message, String actionUrl, ZonedDateTime sentAt) {
}
