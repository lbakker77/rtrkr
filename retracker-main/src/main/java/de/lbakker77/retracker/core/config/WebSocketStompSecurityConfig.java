package de.lbakker77.retracker.core.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.util.AntPathMatcher;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
public class WebSocketStompSecurityConfig {
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpSubscribeDestMatchers("/topic/retracker/*").authenticated()
                .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
                .anyMessage().denyAll();

        return messages.build();
    }

    @Bean
    @Scope("prototype")
    MessageMatcherDelegatingAuthorizationManager.Builder messageAuthorizationManagerBuilder(ApplicationContext context) {
        return MessageMatcherDelegatingAuthorizationManager.builder().simpDestPathMatcher(() -> (context.getBeanNamesForType(SimpAnnotationMethodMessageHandler.class).length > 0 ? (context.getBean(SimpAnnotationMethodMessageHandler.class)).getPathMatcher() : new AntPathMatcher()));
    }
}
