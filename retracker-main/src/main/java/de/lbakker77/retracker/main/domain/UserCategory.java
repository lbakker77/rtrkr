package de.lbakker77.retracker.main.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserCategory(String categoryName, String categoryColor) {

}
