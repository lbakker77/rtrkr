package de.lbakker77.retracker.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.TimeZone;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    public UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");
            String sub = jwt.getSubject();
            
            log.info("User: {} {} ({})", firstName, lastName, email);
            return UUID.fromString("00000000-0000-0000-0000-000000000001");
        }
        throw new IllegalStateException("User not authenticated or JWT not found");
    }

    public TimeZone getUserTimeZone() {
        return TimeZone.getTimeZone("Europe/Berlin");
    }
}
