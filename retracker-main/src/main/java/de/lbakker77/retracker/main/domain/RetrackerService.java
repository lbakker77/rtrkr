package de.lbakker77.retracker.main.domain;

import de.lbakker77.retracker.core.exception.ForbiddenException;
import de.lbakker77.retracker.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RetrackerService {
    private final TaskRepository taskRepository;
    private final RetrackerListRepository retrackerListRepository;

    public Task loadTaskAndEnsureAccess(UUID entryId, UUID userId) {
        var optionalEntry = taskRepository.findById(entryId);
        var entry = optionalEntry.orElseThrow(() -> new NotFoundException("Task with id " + entryId + " not found"));
        var list = entry.getRetrackerList();
        if (!list.hasAccess(userId)){
            throw new ForbiddenException("User not authorized to change this Task id: " + entryId);
        }

        return entry;
    }

    public RetrackerList loadRetrackerListAndEnsureAccess(UUID listId, UUID userId) {
        var list = retrackerListRepository.findById(listId).orElseThrow(() -> new NotFoundException("Retracker list with id " + listId + " not found"));
        if (!list.hasAccess(userId)){
            throw new ForbiddenException("User not authorized to access this retracker list id: " + listId);
        }
        return list;
    }

    public RetrackerList loadListAndEnsureUserIsInvited(UUID listId, UUID userId) {
        var list = retrackerListRepository.findById(listId).orElseThrow(() -> new NotFoundException("Retracker list with id " + listId + " not found"));
        if (!list.isInvited(userId) && !list.hasAccess(userId)){
            throw new ForbiddenException("User not authorized to access this retracker list id: " + listId);
        }
        return list;
    }



    public Set<UUID> getSharedUserIds(UUID userId) {
        List<RetrackerList> ownedLists = retrackerListRepository.findByOwnerId(userId);
        List<RetrackerList> sharedWithUserLists = retrackerListRepository.findByShareConfigEntriesSharedWithUserId(userId);

        var sharedUserIds = ownedLists.stream()
                .flatMap(list -> list.getShareConfigEntries().stream())
                .map(ShareConfig::getSharedWithUserId).toList();

        var ownersOfSharedLists = sharedWithUserLists.stream()
                .map(RetrackerList::getOwnerId)
                .toList();

        return Stream.concat(sharedUserIds.stream(), ownersOfSharedLists.stream())
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toSet());
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public RetrackerList save(RetrackerList list) {
        return retrackerListRepository.save(list);
    }

    public void delete(Task entry) {
        taskRepository.delete(entry);
    }
}