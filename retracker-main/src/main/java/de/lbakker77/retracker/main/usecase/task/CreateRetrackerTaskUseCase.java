package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.domain.TaskCreator;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import de.lbakker77.retracker.core.usercase.CreatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRetrackerTaskUseCase extends BaseUseCaseHandler<CreateRetrackerTaskRequest, CreatedResponse> {
    private final ApplicationEventPublisher events;
    private final RetrackerService retrackerService;
    private final RetrackerMapper retrackerMapper;

    @Override
    protected CreatedResponse handle(CreateRetrackerTaskRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());

        var dueDate = request.getDueDate() != null ? request.getDueDate().withZoneSameInstant(commandContext.getZoneId()).toLocalDate() : null;
        var lastEntryDate = request.getLastEntryDate() != null ? request.getLastEntryDate().withZoneSameInstant(commandContext.getZoneId()).toLocalDate() : null;

        var recurrenceConfig = retrackerMapper.toRecurrenceConfig(request.getRecurrenceConfig());
        var userCategory = retrackerMapper.toUserCategory(request.getUserCategory());
        var task = TaskCreator.createTask(list, request.getName(), dueDate, lastEntryDate, userCategory, recurrenceConfig );

        retrackerService.save(task);

        return new CreatedResponse(task.getId());
    }
}
