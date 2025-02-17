package de.lbakker77.retracker.main.user.usecase.dto;

import java.util.UUID;

public record UserDto(UUID id, String email, String firstName, String lastName) {
}