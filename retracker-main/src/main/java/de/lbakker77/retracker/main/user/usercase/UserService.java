package de.lbakker77.retracker.main.user.usercase;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    public UUID getUserId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");

    }
}
