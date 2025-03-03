package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.*;
import de.lbakker77.retracker.main.domain.RetrackerService;
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
            throw new BadRequestException("Only the owner or the user to remove can remove shared access");
        }

        list.removeAccess(request.getUserIdToRemove());

        retrackerService.save(list);
        return BaseResponse.ofSuccess();
    }
}