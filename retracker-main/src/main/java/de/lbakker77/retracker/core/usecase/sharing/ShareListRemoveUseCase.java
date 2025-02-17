package de.lbakker77.retracker.core.usecase.sharing;

import de.lbakker77.retracker.core.domain.RetrackerService;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
import de.lbakker77.retracker.shared.usercase.Violation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareListRemoveUseCase extends BaseUseCaseHandler<ShareListRemoveRequest, BaseResponse> {
    private final RetrackerService retrackerService;

    @Override
    protected BaseResponse handle(ShareListRemoveRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());

        if (!list.getOwnerId().equals(commandContext.userId()) && !request.getUserIdToRemove().equals(commandContext.userId() )) {
            return BaseResponse.ofFailure(List.of(new Violation("listId", "Only the owner or the user himself can remove shared access")));
        }

        list.removeAccess(request.getUserIdToRemove());

        retrackerService.save(list);
        return BaseResponse.ofSuccess();
    }
}