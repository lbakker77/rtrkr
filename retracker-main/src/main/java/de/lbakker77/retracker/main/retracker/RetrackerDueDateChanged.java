package de.lbakker77.retracker.main.retracker;

import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

public record RetrackerDueDateChanged(RetrackerEntry entry, LocalDate formerDueDate) {}
