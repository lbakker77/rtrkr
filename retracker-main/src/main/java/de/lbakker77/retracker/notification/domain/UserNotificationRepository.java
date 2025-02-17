package de.lbakker77.retracker.notification.domain;


import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserNotificationRepositor extends CrudRepository<UserNotification, UUID> {
}
