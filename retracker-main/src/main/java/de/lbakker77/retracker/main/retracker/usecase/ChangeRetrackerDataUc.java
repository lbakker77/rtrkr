package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.entity.RetrackerEntryRepository;
import de.lbakker77.retracker.main.retracker.entity.model.RecurrenceConfig;
import de.lbakker77.retracker.main.retracker.entity.model.UserCategory;
import de.lbakker77.retracker.main.retracker.usecase.service.RetrackerService;
import de.lbakker77.retracker.main.shared.usercase.BaseResponse;
import de.lbakker77.retracker.main.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.main.shared.usercase.CommandContext;
import de.lbakker77.retracker.main.shared.exception.BadRequestException;
import de.lbakker77.retracker.main.shared.exception.ForbiddenException;
import de.lbakker77.retracker.main.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeRetrackerDataUc extends BaseUseCaseHandler<ChangeRetrackerDataRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    public BaseResponse handle(ChangeRetrackerDataRequest request, CommandContext context) {
        var entry = retrackerService.loadEntryAndEnsureAccess(request.getId(), context.userId());
        entry.setName(request.getName());
        if (request.getRecurrenceConfig() != null){
            entry.setRecurrenceConfig(new RecurrenceConfig(request.getRecurrenceConfig().recurrenceInterval(), request.getRecurrenceConfig().recurrenceTimeUnit()));
        }else {
            entry.setRecurrenceConfig(null);
        }
        entry.setUserCategory(new UserCategory(request.getUserCategory().categoryName(), request.getUserCategory().categoryColor()));
        retrackerService.save(entry);
        return new BaseResponse(true);
    }




}
