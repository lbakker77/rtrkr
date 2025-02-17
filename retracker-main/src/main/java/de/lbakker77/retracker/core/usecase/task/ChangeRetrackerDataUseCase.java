package de.lbakker77.retracker.core.usecase.task;

import de.lbakker77.retracker.core.domain.RetrackerService;
import de.lbakker77.retracker.core.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeRetrackerDataUseCase extends BaseUseCaseHandler<ChangeRetrackerDataRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final RetrackerMapper retrackerMapper;

    public BaseResponse handle(ChangeRetrackerDataRequest request, CommandContext context) {
        var entry = retrackerService.loadTaskAndEnsureAccess(request.getId(), context.userId());
        var formerDueDate = entry.getDueDate();
        entry.changeName(request.getName());
        if (request.getRecurrenceConfig() != null){
            var recurrenceConfig = retrackerMapper.toRecurrenceConfig(request.getRecurrenceConfig());
            entry.updateRecurrenceConfig(recurrenceConfig);
        }else {
            entry.deleteRecurrenceConfig();
        }
        if ((formerDueDate == null && entry.getDueDate() != null) ||
                (formerDueDate != null && !formerDueDate.equals(entry.getDueDate()))) {
            //events.publishEvent(new RetrackerDueDateChanged(entry, formerDueDate));
        }

        entry.changeUserCategory(request.getUserCategory().categoryName(), request.getUserCategory().categoryColor());
        retrackerService.save(entry);
        return BaseResponse.ofSuccess();
    }

}
