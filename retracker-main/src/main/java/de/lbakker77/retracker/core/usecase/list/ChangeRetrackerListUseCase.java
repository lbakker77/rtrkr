package de.lbakker77.retracker.core.usecase.list;

import de.lbakker77.retracker.core.domain.RetrackerList;
import de.lbakker77.retracker.core.domain.RetrackerListRepository;
import de.lbakker77.retracker.shared.exception.NotFoundException;
import de.lbakker77.retracker.shared.usercase.BaseResponse;
import de.lbakker77.retracker.shared.usercase.BaseUseCaseHandler;
import de.lbakker77.retracker.shared.usercase.CommandContext;
import de.lbakker77.retracker.shared.usercase.Violation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangeRetrackerListUseCase extends BaseUseCaseHandler<ChangeRetrackerListRequest, BaseResponse> {

    private final RetrackerListRepository retrackerListRepository;

    @Override
    public BaseResponse handle(ChangeRetrackerListRequest request, CommandContext commandContext) {
        Optional<RetrackerList> optionalList = retrackerListRepository.findById(request.getId());

        if (optionalList.isEmpty()) {
            throw new NotFoundException("Retracker list not found");
        }
        RetrackerList existingList = optionalList.get();

        if (!existingList.getOwnerId().equals(commandContext.userId())) {
            return BaseResponse.ofFailure(List.of(new Violation("", "You don't have permission to modify this list")));
        }

        existingList.setName(request.getName());
        existingList.setIcon(request.getIcon());

        retrackerListRepository.save(existingList);

        return BaseResponse.ofSuccess();
    }
}