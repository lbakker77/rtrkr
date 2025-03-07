package de.lbakker77.retracker.user;

import de.lbakker77.retracker.user.entity.UserRepository;
import de.lbakker77.retracker.user.entity.model.User;
import de.lbakker77.retracker.user.usecase.ActivateUserUseCase;
import de.lbakker77.retracker.user.usecase.InviteUserUseCase;
import de.lbakker77.retracker.user.usecase.NewUserUseCase;
import de.lbakker77.retracker.user.usecase.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    public static final String EMAIL_CLAIM = "email";
    private final UserRepository userRepository;
    private final NewUserUseCase newUserUseCase;
    private final Keycloak keyCloak;
    private final InviteUserUseCase inviteUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final UserMapper userMapper;

    public UUID getOrCreateUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString(EMAIL_CLAIM);

            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");
            var user = userRepository.findByEmail(email).orElseGet(() -> newUserUseCase.createNewUser(email, firstName, lastName));
            if (!user.isActive()) {
                activateUserUseCase.activateUser(user, firstName, lastName);
            }
            return user.getId();
        }
        throw new IllegalStateException("User not authenticated");
    }

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString(EMAIL_CLAIM);
            var user = userRepository.findByEmail(email);
            if (user.isEmpty()){
                throw new IllegalStateException("User not found");
            }
            return userMapper.userToUserDto(user.get());
        } else {
            throw new IllegalStateException("User not authenticated or JWT not found");
        }
    }

    public Optional<UUID> getOrCreateUserId(String email) {
        return userRepository.findByEmail(email).map(User::getId);
    }

    public UUID inviteUser(String email) {
        var id = userRepository.findByEmail(email).map(User::getId);
        if (id.isPresent()) throw new IllegalStateException("User already exists");
        var user = inviteUserUseCase.inviteUser(email);
        return user.getId();
    }

    public List<UserDto> retrieveUserInfos(Collection<UUID> userIds) {
        var users = userRepository.findByIdIn(userIds);
        return users.stream().map(userMapper::userToUserDto).toList();
    }

    public List<UserDto> findUsersByFullNameOrEmail(String exactNameOrEmail) {
        if (exactNameOrEmail == null || exactNameOrEmail.isEmpty()) throw new IllegalArgumentException("Exact name or email must not be null or empty");

        if (exactNameOrEmail.contains("@")){
            return keyCloak.realm("retracker").users().searchByEmail(exactNameOrEmail, true).stream().map(u -> new UserDto(UUID.fromString(u.getId()), u.getEmail(), u.getFirstName(), u.getLastName())).toList();
        } else {
            if (exactNameOrEmail.contains(" ")){
                var names = exactNameOrEmail.split(" ");
                var firstName = names[0];
                var lastName = names.length > 1? names[names.length-1] : null;
                return keyCloak.realm("retracker").users().searchByAttributes(String.format("firstName:%s lastName:%s", firstName, lastName),false).stream().map(u -> new UserDto(UUID.fromString(u.getId()), u.getEmail(), u.getFirstName(), u.getLastName())).toList();
            }
        }
        return List.of();
    }

    public Locale getPreferredLocale() {
        return LocaleContextHolder.getLocale(); // Load locale from request
    }
}
