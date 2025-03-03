package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.Task;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ChangeTaskDataUseCase extends BaseTaskChangeUseCase<ChangeTaskDataRequest, BaseResponse> {
    private final RetrackerMapper retrackerMapper;

    public ChangeTaskDataUseCase(RetrackerService retrackerService, ApplicationEventPublisher events, RetrackerMapper retrackerMapper) {
        super(retrackerService, events);
        this.retrackerMapper = retrackerMapper;
    }


    @Override
    protected BaseResponse handleTaskChange(Task task, ChangeTaskDataRequest request, CommandContext context) {
        task.changeName(request.getName());
        if (request.getRecurrenceConfig() != null){
            var recurrenceConfig = retrackerMapper.toRecurrenceConfig(request.getRecurrenceConfig());
            task.updateRecurrenceConfig(recurrenceConfig);
        }else {
            task.deleteRecurrenceConfig();
        }

        task.changeCategory(request.getCategory());

        retrackerService.save(task);
        return BaseResponse.ofSuccess();
    }
}
