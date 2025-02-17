package de.lbakker77.retracker.core.usecase.sharing;

import de.lbakker77.retracker.core.domain.RetrackerService;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcceptInvitationUseCase extends BaseUseCaseHandler<AcceptInvitationRequest, BaseResponse> {
    private final RetrackerService retrackerService;

    @Override
    protected BaseResponse handle(AcceptInvitationRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());
        var userId = commandContext.userId();

        list.acceptInvitation(userId);
        retrackerService.save(list);

        return BaseResponse.ofSuccess();
    }
}