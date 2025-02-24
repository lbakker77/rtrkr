package de.lbakker77.retracker.main.controller;

import de.lbakker77.retracker.main.TaskChangedEvent;
import de.lbakker77.retracker.main.TaskCreatedEvent;
import de.lbakker77.retracker.main.TaskDeletedEvent;
import de.lbakker77.retracker.main.usecase.dtos.ChangeType;
import de.lbakker77.retracker.main.usecase.dtos.TaskChangeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RetrackerWebSocketHandler {
    private final SimpMessagingTemplate template;
    private final String TOPIC_BASE = "/topic/retracker/";

    @EventListener
    void on(TaskChangedEvent event) {
        // Check if the due date has changed by at least 2 days due to different time zones
        var dueCountChangePossible = false;
        var dueDateUnchanged = (event.formerDueDate() == null && event.task().getDueDate() == null) || event.task().getDueDate() != null && event.task().getDueDate().equals(event.formerDueDate());
        if (!dueDateUnchanged) {
            var formerDatePotentiallyDue = event.formerDueDate() != null && event.formerDueDate().isBefore(LocalDate.now().plusDays(2));
            var newDatePotentiallyDue = event.task().getDueDate() != null && event.task().getDueDate().isBefore(LocalDate.now().plusDays(2));
            dueCountChangePossible = (formerDatePotentiallyDue || newDatePotentiallyDue) && !(formerDatePotentiallyDue && newDatePotentiallyDue);
        }
        template.convertAndSend(TOPIC_BASE + event.list().getId(), new TaskChangeEventDto(event.task().getId(), ChangeType.CHANGED, dueCountChangePossible, event.userId() ));
    }

    @EventListener
    void on(TaskDeletedEvent event) {
        var dueCountChangePossible = event.deletedTask().getDueDate()!= null && event.deletedTask().getDueDate().isBefore(LocalDate.now().plusDays(2));
        template.convertAndSend(TOPIC_BASE + event.list().getId(), new TaskChangeEventDto(event.deletedTask().getId(), ChangeType.DELETED, dueCountChangePossible, event.userId()));
    }

    @EventListener
    void on(TaskCreatedEvent event) {
        var dueCountChangePossible = event.task().getDueDate()!= null && event.task().getDueDate().isBefore(LocalDate.now().plusDays(2));
        template.convertAndSend(TOPIC_BASE + event.list().getId(), new TaskChangeEventDto(event.task().getId(), ChangeType.CREATED, dueCountChangePossible, event.userId()));
    }

}
