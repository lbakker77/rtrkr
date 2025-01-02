package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.usecase.service.RetrackerService;
import de.lbakker77.retracker.main.shared.usercase.BaseResponse;
import de.lbakker77.retracker.main.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.main.shared.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarkRetrackerEntryDoneUseCase extends BaseUseCaseHandler<MarkRetrackerEntryDoneRequest, BaseResponse> {
    private final RetrackerService retrackerService;

    @Override
    protected BaseResponse handle(MarkRetrackerEntryDoneRequest request, CommandContext commandContext) {
        var entry = retrackerService.loadEntryAndEnsureAccess(request.getId(), commandContext.userId());
        entry.registerCompletion(request.getDoneAt());
        retrackerService.save(entry);
        return new BaseResponse(true);
    }
}
