package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.RetrackerDueDateChanged;
import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;
import de.lbakker77.retracker.main.retracker.usecase.CreateRetrackerEntryRequest;
import de.lbakker77.retracker.main.retracker.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.main.retracker.usecase.service.RetrackerService;
import de.lbakker77.retracker.main.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.main.shared.usercase.CommandContext;
import de.lbakker77.retracker.main.shared.usercase.CreatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class CreateRetrackerUseCase extends BaseUseCaseHandler<CreateRetrackerEntryRequest, CreatedResponse> {
    private final ApplicationEventPublisher events;
    private final RetrackerService retrackerService;
    private final RetrackerMapper retrackerMapper;
    @Override
    protected CreatedResponse handle(CreateRetrackerEntryRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());

        var entry = retrackerMapper.toRetrackerEntry(request);
        entry.setRetrackerList(list);

        if (request.getDueDate()!= null) {
            entry.setDueDate(request.getDueDate().withZoneSameInstant(commandContext.getZoneId()).toLocalDate());
        }
        else if (request.getLastEntryDate()!= null && entry.getRecurrenceConfig() != null) {
            entry.setDueDate(entry.getRecurrenceConfig().calcRecurrenceDate(request.getLastEntryDate().withZoneSameInstant(commandContext.getZoneId()).toLocalDate()));
        }
        retrackerService.save(entry);
        events.publishEvent(new RetrackerDueDateChanged(entry, null));

        return new CreatedResponse(entry.getId());
    }
}
