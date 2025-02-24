package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import de.lbakker77.retracker.main.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class UndoLastCompletionUseCase extends BaseTaskChangeUseCase<UndoLastCompletionRequest, BaseResponse> {

    @Override
    protected BaseResponse handleTaskChange(Task task, UndoLastCompletionRequest request, CommandContext context) {
        task.undoLastCompletion();
        retrackerService.save(task);
        return BaseResponse.ofSuccess();
    }
}
