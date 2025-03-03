package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usecase.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.usecase.constants.NotificationConstants;
import de.lbakker77.retracker.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RejectInvitationUseCase extends BaseUseCaseHandler<RejectInvitationRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final NotificationService notificationService;

    @Override
    protected BaseResponse handle(RejectInvitationRequest request, CommandContext commandContext) {
        var list = retrackerService.loadListAndEnsureUserIsInvited(request.getListId(), commandContext.userId());
        var userId = commandContext.userId();

        list.rejectInvitation(userId);

        retrackerService.save(list);
        notificationService.deleteInAppNotification(NotificationConstants.createInviteKey(list.getId(), userId));

        return BaseResponse.ofSuccess();
    }
}