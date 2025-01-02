package de.lbakker77.retracker.main.retracker.entity.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserCategory(String categoryName, UserCategoryColor categoryColor) {

}
