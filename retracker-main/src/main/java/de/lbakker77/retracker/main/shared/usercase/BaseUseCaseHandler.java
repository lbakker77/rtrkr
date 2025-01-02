package de.lbakker77.retracker.main.shared.usercase;

import de.lbakker77.retracker.main.shared.interceptor.UserTimeZoneService;
import de.lbakker77.retracker.main.user.usercase.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseUseCaseHandler<Command extends BaseRequest, Response extends BaseResponse> {
    @Autowired
    private UserService userService;

    @Autowired
    private UserTimeZoneService userTimeZoneService;


    public Response handle(Command command) {
        return handle(command, new CommandContext(userService.getUserId(), userTimeZoneService.getUserTimeZone()));
    }

    protected abstract Response handle(Command command, CommandContext commandContext);
}
