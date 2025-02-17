package de.lbakker77.retracker.core.usecase.sharing;

import de.lbakker77.retracker.core.domain.RetrackerService;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RejectInvitationUseCase extends BaseUseCaseHandler<RejectInvitationRequest, BaseResponse> {
    private final RetrackerService retrackerService;

    @Override
    protected BaseResponse handle(RejectInvitationRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());
        var userId = commandContext.userId();

        list.rejectInvitation(userId);

        retrackerService.save(list);

        return BaseResponse.ofSuccess();
    }
}