package de.lbakker77.retracker.core.usecase.sharing;

import de.lbakker77.retracker.core.domain.RetrackerService;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
import de.lbakker77.retracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteToListUseCase extends BaseUseCaseHandler<InviteToListRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final UserService userService;


    @Override
    protected BaseResponse handle(InviteToListRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());
        var existingUser = userService.getUserId(request.getEmail());
        UUID userId;
        if (existingUser.isEmpty()) {
            userId = userService.InviteUser(request.getEmail());
            // send invitation email here

        } else {
            userId = existingUser.get();
        }

       list.inviteUser(userId);

        if (!list.isShared()) {
            list.setShared(true);
        }
        retrackerService.save(list);
        return BaseResponse.ofSuccess();
    }
}
