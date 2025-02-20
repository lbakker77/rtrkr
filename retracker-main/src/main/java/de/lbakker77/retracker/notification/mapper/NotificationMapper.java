package de.lbakker77.retracker.notification.mapper;

import de.lbakker77.retracker.notification.domain.UserNotification;
import de.lbakker77.retracker.notification.dto.UserNotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING  )
public interface NotificationMapper {
    List<UserNotificationDto> toUserNotificationDtos(List<UserNotification> userNotifications);
}