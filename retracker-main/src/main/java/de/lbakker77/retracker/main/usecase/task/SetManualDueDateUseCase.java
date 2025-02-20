package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetManualDueDateUseCase extends BaseUseCaseHandler<SetManualDueDateRequest, BaseResponse> {

    private final RetrackerService retrackerService;

    @Override
    public BaseResponse handle(SetManualDueDateRequest request, CommandContext context) {
        var task = retrackerService.loadTaskAndEnsureAccess(request.getTaskId(), context.userId());

        task.setManualDueDate(request.getManualDueDate().withZoneSameInstant(context.getZoneId()).toLocalDate());
        retrackerService.save(task);
        return BaseResponse.ofSuccess();
    }
}