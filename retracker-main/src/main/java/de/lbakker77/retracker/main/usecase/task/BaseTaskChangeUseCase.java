package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import de.lbakker77.retracker.main.TaskChangedEvent;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public abstract class BaseTaskChangeUseCase<Request extends BaseTaskChangeRequest, Response extends BaseResponse> extends BaseUseCaseHandler<Request, Response> {

    protected RetrackerService retrackerService;
    private ApplicationEventPublisher events;

    @Autowired
    public void setRetrackerService(RetrackerService retrackerService) {
        this.retrackerService = retrackerService;
    }

    @Autowired
    public void setEvents(ApplicationEventPublisher events) {
        this.events = events;
    }


    public Response handle(Request request, CommandContext context) {
        var task = retrackerService.loadTaskAndEnsureAccess(request.getId(), context.userId());
        var formerDueDate = task.getDueDate();
        var response = handleTaskChange(task, request, context);
        events.publishEvent(new TaskChangedEvent(task.getRetrackerList(), task, formerDueDate, context.userId()));
        return response;
    }

    protected abstract Response handleTaskChange(Task entry, Request request, CommandContext context);

}
