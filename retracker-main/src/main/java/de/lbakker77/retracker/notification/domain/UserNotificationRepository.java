package de.lbakker77.retracker.notification.domain;


import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserNotificationRepository extends CrudRepository<UserNotification, UUID> {
    void deleteByKey(String key);

    List<UserNotification> findByUserIdAndReadFalse(UUID userId);
}
