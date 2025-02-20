package de.lbakker77.retracker.notification.controller;

import de.lbakker77.retracker.core.usercase.UseCaseExecutor;
import de.lbakker77.retracker.notification.dto.UserNotificationDto;
import de.lbakker77.retracker.notification.usecase.UserNotificationReader;
import de.lbakker77.retracker.notification.usecase.MarkAsReadRequest;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor()
public class UserNotificationController {
    private final UserNotificationReader userNotificationReader;
    private final UseCaseExecutor useCaseExecutor;

    @GetMapping()
    public List<UserNotificationDto> userNotifications() {
        return userNotificationReader.getUserNotifications();
    }

    @PostMapping("/{id}/mark-as-read")
    public BaseResponse markAsRead(@PathVariable UUID id) {
        return useCaseExecutor.execute(new MarkAsReadRequest(id));
    }
}