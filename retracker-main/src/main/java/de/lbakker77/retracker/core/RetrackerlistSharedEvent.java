package de.lbakker77.retracker.core;

import java.util.UUID;

public record RetrackerlistSharedEvent(UUID listId, UUID userId) {
}
