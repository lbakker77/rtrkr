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
public class MarkRetrackerTaskDoneUseCase extends BaseTaskChangeUseCase<MarkRetrackerTaskDoneRequest, BaseResponse> {

    @Override
    protected BaseResponse handleTaskChange(Task entry, MarkRetrackerTaskDoneRequest request, CommandContext context) {
        entry.registerCompletion(request.getDoneAt().withZoneSameInstant(context.getZoneId()).toLocalDate());
        retrackerService.save(entry);

        return BaseResponse.ofSuccess();
    }
}
