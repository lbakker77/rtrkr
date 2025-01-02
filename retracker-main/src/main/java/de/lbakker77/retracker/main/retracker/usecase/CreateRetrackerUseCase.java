package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;
import de.lbakker77.retracker.main.retracker.usecase.CreateRetrackerEntryRequest;
import de.lbakker77.retracker.main.retracker.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.main.retracker.usecase.service.RetrackerService;
import de.lbakker77.retracker.main.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.main.shared.usercase.CommandContext;
import de.lbakker77.retracker.main.shared.usercase.CreatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class CreateRetrackerUseCase extends BaseUseCaseHandler<CreateRetrackerEntryRequest, CreatedResponse> {

    private final RetrackerService retrackerService;
    private final RetrackerMapper retrackerMapper;
    @Override
    protected CreatedResponse handle(CreateRetrackerEntryRequest request, CommandContext commandContext) {
        var list = retrackerService.loadRetrackerListAndEnsureAccess(request.getListId(), commandContext.userId());

        var entry = retrackerMapper.toRetrackerEntry(request);
        entry.setRetrackerList(list);

        if (request.getDueDate()!= null) {
            entry.setDueDate(request.getDueDate());
        }
        else if (request.getLastEntryDate()!= null) {
            entry.registerCompletion(request.getLastEntryDate());
        }
        retrackerService.save(entry);

        return new CreatedResponse(entry.getId());
    }
}
