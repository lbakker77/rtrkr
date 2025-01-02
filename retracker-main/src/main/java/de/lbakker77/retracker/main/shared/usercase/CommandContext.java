package de.lbakker77.retracker.main.shared.usercase;

import java.util.TimeZone;
import java.util.UUID;

public record CommandContext(UUID userId, TimeZone userTimeZone) {
}
