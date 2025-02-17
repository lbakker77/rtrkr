package de.lbakker77.retracker.main.user.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakConfig {
    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8081")
                .realm("retracker")
                .clientId("retracker-backend")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret("dJsmyl4nMQBvZljNRqle0a9YFsJYxKOs")
                .build();
    }

}
