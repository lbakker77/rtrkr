package de.lbakker77.retracker.main.usecase.list;

import de.lbakker77.retracker.main.domain.RetrackerListCreator;
import de.lbakker77.retracker.main.domain.RetrackerListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateDefaultRetrackerListUseCase {
    private final RetrackerListRepository retrackerListRepository;
    private final RetrackerListCreator retrackerListCreator;

    public void initDataForNewUser(UUID id) {
        var defaultList = retrackerListCreator.createDefaultList(id);
        retrackerListRepository.save(defaultList);
    }
}
