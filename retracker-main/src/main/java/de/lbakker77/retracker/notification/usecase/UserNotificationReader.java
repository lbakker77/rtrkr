package de.lbakker77.retracker.notification.usecase;

import de.lbakker77.retracker.notification.domain.UserNotificationRepository;
import de.lbakker77.retracker.notification.dto.UserNotificationDto;
import de.lbakker77.retracker.notification.mapper.NotificationMapper;
import de.lbakker77.retracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationReader {

    private final UserNotificationRepository userNotificationRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    public List<UserNotificationDto> getUserNotifications() {
        var notifications = userNotificationRepository.findByUserIdAndReadFalse(userService.getUserIdOrCreateIfNew());
        return notificationMapper.toUserNotificationDtos(notifications);
    }
}
