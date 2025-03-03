package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseUseCaseHandler;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.core.CreatedResponse;
import de.lbakker77.retracker.main.TaskCreatedEvent;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.TaskCreator;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase extends BaseUseCaseHandler<CreateTaskRequest, CreatedResponse> {
    private final ApplicationEventPublisher events;
    private final RetrackerService retrackerService;
    private final RetrackerMapper retrackerMapper;
    private final TaskCreator taskCreator;

    @Override
    protected CreatedResponse handle(CreateTaskRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());

        var dueDate = request.getDueDate() != null ? request.getDueDate().withZoneSameInstant(commandContext.getZoneId()).toLocalDate() : null;
        var lastEntryDate = request.getLastEntryDate() != null ? request.getLastEntryDate().withZoneSameInstant(commandContext.getZoneId()).toLocalDate() : null;

        var recurrenceConfig = retrackerMapper.toRecurrenceConfig(request.getRecurrenceConfig());
        var task = taskCreator.createTask(list, request.getName(), dueDate, lastEntryDate, request.getCategory(), recurrenceConfig );

        retrackerService.save(task);
        events.publishEvent(new TaskCreatedEvent(task.getRetrackerList(), task, commandContext.userId()));

        return new CreatedResponse(task.getId());
    }
}
