package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.BaseUseCaseHandler;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.usecase.constants.NotificationConstants;
import de.lbakker77.retracker.notification.NotificationService;
import de.lbakker77.retracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteToListUseCase extends BaseUseCaseHandler<InviteToListRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final MessageSource messageSource;


    @Override
    protected BaseResponse handle(InviteToListRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());
        var existingUser = userService.getUserId(request.getEmail());
        UUID userId;
        userId = existingUser.orElseGet(() -> userService.inviteUser(request.getEmail()));

        list.inviteUser(userId);
        sendNotification(list, userId);

        retrackerService.save(list);
        return BaseResponse.ofSuccess();
    }

    private void sendNotification(RetrackerList list, UUID userId) {
        var user = userService.getCurrentUser();
        var name = user.firstName() + " " + user.lastName();
        var message = messageSource.getMessage("notification.invite_to_list", new Object[]{name, list.getName()}, userService.getPreferredLocale());
        var notificationKey = NotificationConstants.createInviteKey(list.getId(), userId);
        var commandString = NotificationConstants.createInviteActionCommand(list.getId());
        notificationService.sendInAppNotification(notificationKey, userId, message, commandString);
    }
}
