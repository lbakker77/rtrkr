package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import de.lbakker77.retracker.main.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PostponeRetrackerTaskUseCase extends BaseTaskChangeUseCase<PostponeRetrackerTaskRequest, BaseResponse> {

    @Override
    protected BaseResponse handleTaskChange(Task entry, PostponeRetrackerTaskRequest request, CommandContext context) {
        var postponedDate = request.getPostponedDate().withZoneSameInstant(context.userTimeZone().toZoneId()).toLocalDate();
        entry.postponeUntil(postponedDate);
        retrackerService.save(entry);
        return BaseResponse.ofSuccess();
    }
}
