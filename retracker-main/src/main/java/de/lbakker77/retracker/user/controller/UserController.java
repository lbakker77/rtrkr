package de.lbakker77.retracker.main.user.controller;

import de.lbakker77.retracker.main.user.UserService;
import de.lbakker77.retracker.main.user.usecase.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor()
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public List<UserDto> findUser(@RequestParam("search") String exactName) {
        return userService.findUsersByFullNameOrEmail(exactName);
    }
}
