package de.lbakker77.retracker.notification.domain;


import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserNotificationRepository extends CrudRepository<UserNotification, UUID> {
    public void deleteByKey(String key);
}
