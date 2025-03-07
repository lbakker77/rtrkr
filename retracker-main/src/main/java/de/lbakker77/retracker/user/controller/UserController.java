package de.lbakker77.retracker.user.controller;

import de.lbakker77.retracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor()
public class UserController {
    private final UserService userService;

    @GetMapping("/id")
    public UUID getOrCreateUserId() {
        return userService.getOrCreateUserId();
    }
}
