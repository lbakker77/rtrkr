package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;
import de.lbakker77.retracker.main.retracker.usecase.service.RetrackerService;
import de.lbakker77.retracker.main.shared.usercase.BaseResponse;
import de.lbakker77.retracker.main.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.main.shared.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRetrackerEntryUseCase extends BaseUseCaseHandler<DeleteRetrackerEntryRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    @Override
    protected BaseResponse handle(DeleteRetrackerEntryRequest deleteRetrackerEntryRequest, CommandContext commandContext) {
        RetrackerEntry entry = retrackerService.loadEntryAndEnsureAccess(deleteRetrackerEntryRequest.getId(), commandContext.userId());
        retrackerService.delete(entry);
        return new BaseResponse(true);
    }
}
