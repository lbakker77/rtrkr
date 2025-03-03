package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.BaseUseCaseHandler;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.main.TaskDeletedEvent;
import de.lbakker77.retracker.main.domain.RetrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCase extends BaseUseCaseHandler<DeleteTaskRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final ApplicationEventPublisher events;

    @Override
    protected BaseResponse handle(DeleteTaskRequest command, CommandContext commandContext) {
        var task = retrackerService.loadTaskAndEnsureAccess(command.getId(), commandContext.userId());
        retrackerService.delete(task);
        events.publishEvent(new TaskDeletedEvent(task.getRetrackerList(), task, task.getDueDate(), commandContext.userId()));
        return BaseResponse.ofSuccess();
    }
}
