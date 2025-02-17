package de.lbakker77.retracker.user;

import de.lbakker77.retracker.user.entity.UserRepository;
import de.lbakker77.retracker.user.entity.model.User;
import de.lbakker77.retracker.user.usecase.ActivateUserUseCase;
import de.lbakker77.retracker.user.usecase.InviteUserUseCase;
import de.lbakker77.retracker.user.usecase.dto.UserDto;
import de.lbakker77.retracker.user.usecase.NewUserUseCase;
import de.lbakker77.retracker.user.usecase.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final NewUserUseCase newUserUseCase;
    private final Keycloak keyCloak;
    private final InviteUserUseCase inviteUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final UserMapper userMapper;

    public UUID getUserIdOrCreateIfNew() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("email");
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");
            var user = userRepository.findByEmail(email).orElseGet(() -> newUserUseCase.createNewUser(email, firstName, lastName));
            if (!user.isActive()) {
                activateUserUseCase.activateUser(user, firstName, lastName);
            }
            return user.getId();
        }
        throw new IllegalStateException("User not authenticated or JWT not found");
    }

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("email");
            var user = userRepository.findByEmail(email);
            if (user.isEmpty()){
                throw new IllegalStateException("User not found");
            }
            return user.get().getId();
        } else {
            throw new IllegalStateException("User not authenticated or JWT not found");
        }
    }

    public Optional<UUID> getUserId(String email) {
        return userRepository.findByEmail(email).map(User::getId);
    }

    public UUID InviteUser(String email) {
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
                var lastName = names.length > 1? names[1] : null;
                return keyCloak.realm("retracker").users().searchByAttributes(String.format("firstName:%s lastName:%s", firstName, lastName),false).stream().map(u -> new UserDto(UUID.fromString(u.getId()), u.getEmail(), u.getFirstName(), u.getLastName())).toList();
            }
        }
        return List.of();
    }
}
