package de.lbakker77.retracker.notification.controller;

import de.lbakker77.retracker.notification.dto.UserNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor()
public class UserNotificationController {

    @GetMapping()
    public List<UserNotificationDto> userNotifications() {

    }
}
