package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;

import de.lbakker77.retracker.main.usecase.constants.NotificationConstants;
import de.lbakker77.retracker.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcceptInvitationUseCase extends BaseUseCaseHandler<AcceptInvitationRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final NotificationService notificationService;

    @Override
    protected BaseResponse handle(AcceptInvitationRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());
        var userId = commandContext.userId();

        list.acceptInvitation(userId);
        retrackerService.save(list);

        notificationService.deleteInAppNotification(NotificationConstants.createInviteKey(list.getId(), userId));

        return BaseResponse.ofSuccess();
    }
}