package de.lbakker77.retracker.core.usecase.task;

import de.lbakker77.retracker.core.domain.RetrackerService;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostponeRetrackerTaskUseCase extends BaseUseCaseHandler<PostponeRetrackerTaskRequest, BaseResponse> {
    private final ApplicationEventPublisher events;
    private final RetrackerService retrackerService;




    @Override
    protected BaseResponse handle(PostponeRetrackerTaskRequest request, CommandContext commandContext) {
        var entry = retrackerService.loadTaskAndEnsureAccess(request.getId(), commandContext.userId());
        var formerDueDate = entry.getDueDate();
        var postponedDate = request.getPostponedDate().withZoneSameInstant(commandContext.userTimeZone().toZoneId()).toLocalDate();
        entry.postponeUntil(postponedDate);
        retrackerService.save(entry);
        //events.publishEvent(new RetrackerDueDateChanged(entry, formerDueDate));

        return BaseResponse.ofSuccess();
    }
}
