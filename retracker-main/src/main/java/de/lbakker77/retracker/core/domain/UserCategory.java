package de.lbakker77.retracker.core.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserCategory(String categoryName, String categoryColor) {

}
