package de.lbakker77.retracker.main;

import java.util.UUID;

public record RetrackerlistSharedEvent(UUID listId, UUID userId) {
}
