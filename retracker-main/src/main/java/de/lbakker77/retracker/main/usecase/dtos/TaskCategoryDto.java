package de.lbakker77.retracker.main.usecase.dtos;

import de.lbakker77.retracker.main.domain.TaskCategory;

public record TaskCategoryDto(TaskCategory category, String categoryName) {
}
