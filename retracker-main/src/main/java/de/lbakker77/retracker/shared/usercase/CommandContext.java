package de.lbakker77.retracker.shared.usercase;

import java.time.ZoneId;
import java.util.TimeZone;
import java.util.UUID;

public record CommandContext(UUID userId, TimeZone userTimeZone) {

    public ZoneId getZoneId() {
        return userTimeZone.toZoneId();
    }
}
