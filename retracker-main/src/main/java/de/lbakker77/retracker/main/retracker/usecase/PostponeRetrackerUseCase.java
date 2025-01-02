package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.usecase.service.RetrackerService;
import de.lbakker77.retracker.main.shared.usercase.BaseResponse;
import de.lbakker77.retracker.main.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.main.shared.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostponeRetrackerUseCase extends BaseUseCaseHandler<PostponeRetrackerRequest, BaseResponse> {
    private final RetrackerService retrackerService;


    @Override
    protected BaseResponse handle(PostponeRetrackerRequest request, CommandContext commandContext) {
        var entry = retrackerService.loadEntryAndEnsureAccess(request.getId(), commandContext.userId());
        entry.postponeUntil(request.getPostponedDate());
        retrackerService.save(entry);
        return new BaseResponse(true);
    }
}
