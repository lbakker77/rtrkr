package de.lbakker77.retracker.main.core.usecase.task;

import de.lbakker77.retracker.main.core.domain.RetrackerService;
import de.lbakker77.retracker.main.shared.usercase.BaseResponse;
import de.lbakker77.retracker.main.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.main.shared.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRetrackerTaskUseCase extends BaseUseCaseHandler<DeleteRetrackerTaskRequest, BaseResponse> {
    private final ApplicationEventPublisher events;
    private final RetrackerService retrackerService;


    @Override
    protected BaseResponse handle(DeleteRetrackerTaskRequest deleteRetrackerEntryRequest, CommandContext commandContext) {
        var entry = retrackerService.loadTaskAndEnsureAccess(deleteRetrackerEntryRequest.getId(), commandContext.userId());
        retrackerService.delete(entry);
        return BaseResponse.ofSuccess();
    }
}
