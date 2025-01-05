package de.lbakker77.retracker.main.retracker.controller;

import de.lbakker77.retracker.main.retracker.RetrackerDueDateChanged;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RetrackerWebSocketHandler {

}
