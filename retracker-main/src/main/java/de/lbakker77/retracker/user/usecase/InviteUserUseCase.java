package de.lbakker77.retracker.user.usecase;

import de.lbakker77.retracker.user.entity.UserRepository;
import de.lbakker77.retracker.user.entity.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InviteUserUseCase {

    private final UserRepository userRepository;


    @Transactional
    public User inviteUser(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = User.builder()
                .email(email)
                .isActive(false)
                .build();

        return userRepository.save(newUser);
    }
}