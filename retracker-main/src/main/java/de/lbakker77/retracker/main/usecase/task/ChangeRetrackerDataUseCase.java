package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.Task;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.core.usercase.CommandContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeRetrackerDataUseCase extends BaseTaskChangeUseCase<ChangeRetrackerDataRequest, BaseResponse> {
    private final RetrackerMapper retrackerMapper;


    @Override
    protected BaseResponse handleTaskChange(Task task, ChangeRetrackerDataRequest request, CommandContext context) {
        task.changeName(request.getName());
        if (request.getRecurrenceConfig() != null){
            var recurrenceConfig = retrackerMapper.toRecurrenceConfig(request.getRecurrenceConfig());
            task.updateRecurrenceConfig(recurrenceConfig);
        }else {
            task.deleteRecurrenceConfig();
        }

        task.changeUserCategory(request.getUserCategory().categoryName(), request.getUserCategory().categoryColor());

        retrackerService.save(task);
        return BaseResponse.ofSuccess();
    }
}
