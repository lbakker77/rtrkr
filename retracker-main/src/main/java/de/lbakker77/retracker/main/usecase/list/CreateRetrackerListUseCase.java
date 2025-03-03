package de.lbakker77.retracker.main.usecase.list;

import de.lbakker77.retracker.core.BaseUseCaseHandler;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.core.CreatedResponse;
import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.RetrackerListCreator;
import de.lbakker77.retracker.main.domain.RetrackerListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRetrackerListUseCase extends BaseUseCaseHandler<CreateRetrackerListRequest, CreatedResponse> {

    private final RetrackerListRepository retrackerListRepository;
    private final RetrackerListCreator retrackerListCreator;

    public CreatedResponse handle(CreateRetrackerListRequest request, CommandContext commandContext) {
        UUID userId = commandContext.userId();

        var newList = retrackerListCreator.createRetrackerList(request.getName(), userId, request.getIcon());

        RetrackerList savedList = retrackerListRepository.save(newList);
        return new CreatedResponse(savedList.getId());
    }
}