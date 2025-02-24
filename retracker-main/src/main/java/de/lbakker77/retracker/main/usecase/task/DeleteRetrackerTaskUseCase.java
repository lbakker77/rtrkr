package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.TaskChangedEvent;
import de.lbakker77.retracker.main.TaskDeletedEvent;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRetrackerTaskUseCase extends BaseUseCaseHandler<DeleteRetrackerTaskRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final ApplicationEventPublisher events;

    @Override
    protected BaseResponse handle(DeleteRetrackerTaskRequest command, CommandContext commandContext) {
        var task = retrackerService.loadTaskAndEnsureAccess(command.getId(), commandContext.userId());
        retrackerService.delete(task);
        events.publishEvent(new TaskDeletedEvent(task.getRetrackerList(), task, task.getDueDate(), commandContext.userId()));
        return BaseResponse.ofSuccess();
    }
}
