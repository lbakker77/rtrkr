package de.lbakker77.retracker.core.usecase.list;

import de.lbakker77.retracker.core.domain.RetrackerList;
import de.lbakker77.retracker.core.domain.RetrackerListRepository;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
import de.lbakker77.retracker.shared.usercase.CreatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRetrackerListUseCase extends BaseUseCaseHandler<CreateRetrackerListRequest, BaseResponse> {

    private final RetrackerListRepository retrackerListRepository;

    public BaseResponse handle(CreateRetrackerListRequest request, CommandContext commandContext) {
        UUID userId = commandContext.userId();

        RetrackerList newList = new RetrackerList();
        newList.setName(request.getName());
        newList.setShared(false);
        newList.setDefaultList(false);
        newList.setIcon(request.getIcon());
        newList.setOwnerId(userId);

        RetrackerList savedList = retrackerListRepository.save(newList);
        return new CreatedResponse(savedList.getId());
    }
}