package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.CommandContext;
import de.lbakker77.retracker.main.domain.Task;
import org.springframework.stereotype.Service;

@Service
public class SetManualDueDateUseCase extends BaseTaskChangeUseCase<SetManualDueDateRequest, BaseResponse> {

    @Override
    protected BaseResponse handleTaskChange(Task task, SetManualDueDateRequest request, CommandContext context) {
        task.setManualDueDate(request.getManualDueDate().withZoneSameInstant(context.getZoneId()).toLocalDate());
        retrackerService.save(task);
        return BaseResponse.ofSuccess();    }
}