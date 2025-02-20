package de.lbakker77.retracker.main.listener;

import de.lbakker77.retracker.main.usecase.NewUserInitUseCase;
import de.lbakker77.retracker.user.NewUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserListener {
    private final NewUserInitUseCase newUserInitUseCase;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void on(NewUserEvent event) {
        log.info("New user registered: {}", event.userId());
        newUserInitUseCase.initDataForNewUser(event.userId());
    }
}
