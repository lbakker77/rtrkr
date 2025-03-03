package de.lbakker77.retracker.user.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakApiAcessConfig {

    @Value("${keycloak.api-access.client-id}")
    private String clientId;

    @Value("${keycloak.api-access.server-url}")
    private String serverUrl;

    @Value("${keycloak.api-access.realm}")
    private String realm;

    @Value("${keycloak.api-access.client-secret}")
    private String clientSecret;

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret(clientSecret)
                .build();
    }

}
