package de.lbakker77.retracker.main.core;

import java.util.UUID;

public record RetrackerlistSharedEvent(UUID listId, UUID userId) {
}
