package de.lbakker77.retracker.user.usecase;

import de.lbakker77.retracker.user.NewUserEvent;
import de.lbakker77.retracker.user.entity.UserRepository;
import de.lbakker77.retracker.user.entity.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewUserUseCase  {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;

    @Transactional
    public User createNewUser(String email, String firstName, String lastName) {
        var user = userRepository.save(User.builder().email(email).firstName(firstName).lastName(lastName).isActive(true).build() );
        events.publishEvent(new NewUserEvent(user.getId()));
        return user;
    }
}
