package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.Task;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PostponeTaskUseCase extends BaseTaskChangeUseCase<PostponeTaskRequest, BaseResponse> {

    public PostponeTaskUseCase(RetrackerService retrackerService, ApplicationEventPublisher events) {
        super(retrackerService, events);
    }

    @Override
    protected BaseResponse handleTaskChange(Task entry, PostponeTaskRequest request, CommandContext context) {
        var postponedDate = request.getPostponedDate().withZoneSameInstant(context.userTimeZone().toZoneId()).toLocalDate();
        entry.postponeUntil(postponedDate);
        retrackerService.save(entry);
        return BaseResponse.ofSuccess();
    }
}
