package de.lbakker77.retracker.main.usecase.list;

import de.lbakker77.retracker.core.ForbiddenException;
import de.lbakker77.retracker.core.NotFoundException;
import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.BaseUseCaseHandler;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.core.Violation;
import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.RetrackerListRepository;
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

        if (!existingList.mayChangeOrDelete(commandContext.userId())) {
            throw new ForbiddenException("User not authorized to change this retracker list");
        }

        if (existingList.isDefaultList()) {
            return BaseResponse.ofFailure(List.of(new Violation("", "Default lists cannot be modified")));
        }

        existingList.changeName(request.getName());
        existingList.changeIcon(request.getIcon());

        retrackerListRepository.save(existingList);

        return BaseResponse.ofSuccess();
    }
}