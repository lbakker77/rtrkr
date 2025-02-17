package de.lbakker77.retracker.notification;

import de.lbakker77.retracker.notification.domain.UserNotification;
import de.lbakker77.retracker.notification.domain.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserNotificationRepository notificationRepository;

    public void sendInAppNotification(String key, UUID userId, String message, String actionUrl) {
        var notification = UserNotification.builder().key(key).userId(userId).message(message).actionUrl(actionUrl).build();
        notificationRepository.save(notification);
    }

    public void deleteInAppNotification(String key) {
        notificationRepository.deleteByKey(key);
    }

}
