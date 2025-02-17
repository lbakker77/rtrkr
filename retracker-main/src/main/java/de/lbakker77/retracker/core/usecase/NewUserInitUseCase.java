package de.lbakker77.retracker.core.usecase;

import de.lbakker77.retracker.core.domain.RetrackerListRepository;
import de.lbakker77.retracker.core.domain.RetrackerList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewUserInitUseCase {
    private final RetrackerListRepository retrackerListRepository;

    public void initDataForNewUser(UUID id) {
        retrackerListRepository.save(RetrackerList.builder().ownerId(id).defaultList(true).name("default").icon("schedule").build());
    }
}
