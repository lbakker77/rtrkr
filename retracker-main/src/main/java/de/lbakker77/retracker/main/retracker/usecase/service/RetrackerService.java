package de.lbakker77.retracker.main.retracker.usecase.service;

import de.lbakker77.retracker.main.retracker.entity.RetrackerEntryRepository;
import de.lbakker77.retracker.main.retracker.entity.RetrackerListRepository;
import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;
import de.lbakker77.retracker.main.retracker.entity.model.RetrackerList;
import de.lbakker77.retracker.main.shared.exception.BadRequestException;
import de.lbakker77.retracker.main.shared.exception.ForbiddenException;
import de.lbakker77.retracker.main.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrackerService {
    private final RetrackerEntryRepository retrackerEntryRepository;
    private final RetrackerListRepository retrackerListRepository;

    public RetrackerEntry loadEntryAndEnsureAccess(UUID entryId, UUID userId) {
        var optionalEntry = retrackerEntryRepository.findById(entryId);
        optionalEntry.orElseThrow(() -> new NotFoundException("Retracker entry with id " + entryId + " not found"));
        var entry = optionalEntry.get();
        var owner = entry.getRetrackerList().getOwnerId();
        if (!owner.equals(userId)) {
            throw new ForbiddenException("User not authorized to change this retracker entry id: " + entryId);
        }

        return entry;
    }

    public RetrackerList loadRetrackerListAndEnsureAccess(UUID listId, UUID userId) {
        var optionalList = retrackerListRepository.findById(listId);
        optionalList.orElseThrow(() -> new NotFoundException("Retracker list with id " + listId + " not found"));
        var list = optionalList.get();
        var owner = list.getOwnerId();
        if (!owner.equals(userId)) {
            throw new ForbiddenException("User not authorized to change this retracker list id: " + listId);
        }
        return list;
    }

    public void save(RetrackerEntry entry) {
        retrackerEntryRepository.save(entry);
    }

    public void delete(RetrackerEntry entry) {
        retrackerEntryRepository.delete(entry);
    }
}
