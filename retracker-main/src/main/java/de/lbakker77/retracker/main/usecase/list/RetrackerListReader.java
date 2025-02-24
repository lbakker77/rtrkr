package de.lbakker77.retracker.main.usecase.list;

import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.TaskRepository;
import de.lbakker77.retracker.main.domain.RetrackerListRepository;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.core.interceptor.UserTimeZoneService;
import de.lbakker77.retracker.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RetrackerListReader {

    private final TaskRepository retrackerTaskRepository;
    private final RetrackerListRepository retrackerListRepository;
    private final RetrackerMapper retrackerMapper;
    private final UserTimeZoneService userTimeZoneService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final RetrackerService retrackerService;



    public List<RetrackerListDto> getAllRetrackerLists() {
        var retrackerListDtos = new LinkedList<RetrackerListDto>();
        UUID userId = userService.getUserIdOrCreateIfNew();
        var retrackerLists = retrackerListRepository.findRetrackerListsForUser(userId);
        var usersCurrentDate = LocalDate.now(userTimeZoneService.getUserTimeZone().toZoneId());
        for (var retrackerList : retrackerLists) {
            var dueCount = retrackerTaskRepository.countByRetrackerListIdAndDueDateLessThanEqual(retrackerList.getId(), usersCurrentDate);
            if (retrackerList.isDefaultList()) {
                retrackerList.setName(messageSource.getMessage("retracker-list.default-name", null, userService.getPreferredLocale()));
            }
            var dto = retrackerMapper.toRetrackerListDto(retrackerList, dueCount, false, userId.equals(retrackerList.getOwnerId()));
            retrackerListDtos.add(dto);
        }
        var invitations = retrackerListRepository.findRetrackerInvitationsForUser(userId);
        for (var retrackerList : invitations) {
            var dueCount = 0L;
            var dto = retrackerMapper.toRetrackerListDto(retrackerList, dueCount, true, false);
            retrackerListDtos.add(dto);
        }
        return retrackerListDtos;
    }

    public long getDueCount(UUID listId) {
        retrackerService.loadRetrackerListAndEnsureAccess(listId, userService.getCurrentUserId());
        var usersCurrentDate = LocalDate.now(userTimeZoneService.getUserTimeZone().toZoneId());
        return retrackerTaskRepository.countByRetrackerListIdAndDueDateLessThanEqual(listId, usersCurrentDate);
    }

}

