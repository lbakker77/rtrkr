package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarkRetrackerTaskDoneUseCase extends BaseUseCaseHandler<MarkRetrackerTaskDoneRequest, BaseResponse> {
    private final ApplicationEventPublisher events;
    private final RetrackerService retrackerService;


    @Override
    protected BaseResponse handle(MarkRetrackerTaskDoneRequest request, CommandContext commandContext) {
        var entry = retrackerService.loadTaskAndEnsureAccess(request.getId(), commandContext.userId());
        entry.registerCompletion(request.getDoneAt().withZoneSameInstant(commandContext.getZoneId()).toLocalDate());
        retrackerService.save(entry);

        return BaseResponse.ofSuccess();
    }
}
