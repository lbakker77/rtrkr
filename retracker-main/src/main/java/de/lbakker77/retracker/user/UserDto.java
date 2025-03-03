package de.lbakker77.retracker.user;

import java.util.UUID;

public record UserDto(UUID id, String email, String firstName, String lastName) {
}