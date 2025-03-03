package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.Task;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MarkTaskDoneUseCase extends BaseTaskChangeUseCase<MarkTaskDoneRequest, BaseResponse> {

    public MarkTaskDoneUseCase(RetrackerService retrackerService, ApplicationEventPublisher events) {
        super(retrackerService, events);
    }

    @Override
    protected BaseResponse handleTaskChange(Task entry, MarkTaskDoneRequest request, CommandContext context) {
        entry.registerCompletion(request.getDoneAt().withZoneSameInstant(context.getZoneId()).toLocalDate());
        retrackerService.save(entry);

        return BaseResponse.ofSuccess();
    }
}
