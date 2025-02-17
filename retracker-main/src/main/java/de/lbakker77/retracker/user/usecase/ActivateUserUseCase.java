package de.lbakker77.retracker.main.user.usecase;

import de.lbakker77.retracker.main.user.NewUserEvent;
import de.lbakker77.retracker.main.user.entity.UserRepository;
import de.lbakker77.retracker.main.user.entity.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivateUserUseCase {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;

    @Transactional
    public void activateUser(User user, String firstName, String lastName) {

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setActive(true);

        userRepository.save(user);
        events.publishEvent(new NewUserEvent(user.getId()));
    }
}
