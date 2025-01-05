package de.lbakker77.retracker.main.retracker.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.ZonedDateTime;
import java.util.UUID;

@Embeddable
public record ShareConfig(@Column(nullable = false) UUID sharedWithUserId, @Column(nullable = false) ShareStatus status, @Column(nullable = false) ZonedDateTime sharedAt) {
}
