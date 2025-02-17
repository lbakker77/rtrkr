package de.lbakker77.retracker.main.notification.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class UserNotification {
    @Id
    private String key;

    private String message;

    private String actionUrl;

    private boolean seen;

    private UUID userId;
}
