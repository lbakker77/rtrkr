package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UndoLastCompletionUseCase extends BaseUseCaseHandler<UndoLastCompletionRequest, BaseResponse> {
    private final RetrackerService retrackerService;

    @Override
    protected BaseResponse handle(UndoLastCompletionRequest command, CommandContext commandContext) {
        var task = retrackerService.loadTaskAndEnsureAccess(command.getId(), commandContext.userId());
        task.undoLastCompletion();
        retrackerService.save(task);
        return BaseResponse.ofSuccess();
    }
}
