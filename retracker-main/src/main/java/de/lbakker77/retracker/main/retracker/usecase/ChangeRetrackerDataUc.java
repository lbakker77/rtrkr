package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.RetrackerDueDateChanged;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeRetrackerDataUc extends BaseUseCaseHandler<ChangeRetrackerDataRequest, BaseResponse> {
    private final RetrackerService retrackerService;
    private final ApplicationEventPublisher events;

    public BaseResponse handle(ChangeRetrackerDataRequest request, CommandContext context) {
        var entry = retrackerService.loadEntryAndEnsureAccess(request.getId(), context.userId());
        var formerDueDate = entry.getDueDate();
        entry.setName(request.getName());
        if (request.getRecurrenceConfig() != null){
            var newRecurrenceConfig = new RecurrenceConfig(request.getRecurrenceConfig().recurrenceInterval(), request.getRecurrenceConfig().recurrenceTimeUnit());
            if (!newRecurrenceConfig.equals(entry.getRecurrenceConfig())) {
                entry.setRecurrenceConfig(newRecurrenceConfig);
                if (entry.getLastEntryDate()!= null) {
                    entry.setPostponedDays(0);
                    entry.setDueDate(newRecurrenceConfig.calcRecurrenceDate(entry.getLastEntryDate()));
                }
            }
        }else {
            if (entry.getRecurrenceConfig() != null) {
                entry.setRecurrenceConfig(null);
                entry.setDueDate(null);
            }
        }
        if ((formerDueDate == null && entry.getDueDate() != null) ||
                (formerDueDate != null && !formerDueDate.equals(entry.getDueDate()))) {
            events.publishEvent(new RetrackerDueDateChanged(entry, formerDueDate));
        }


        entry.setUserCategory(new UserCategory(request.getUserCategory().categoryName(), request.getUserCategory().categoryColor()));
        retrackerService.save(entry);
        return new BaseResponse(true);
    }




}
