package de.lbakker77.retracker.core.usecase.task;

import de.lbakker77.retracker.core.domain.RetrackerService;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
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
