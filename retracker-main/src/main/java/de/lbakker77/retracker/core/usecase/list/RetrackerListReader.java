package de.lbakker77.retracker.main.core.usecase.list;

import de.lbakker77.retracker.main.core.domain.TaskRepository;
import de.lbakker77.retracker.main.core.domain.RetrackerListRepository;
import de.lbakker77.retracker.main.core.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.core.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.main.shared.interceptor.UserTimeZoneService;
import de.lbakker77.retracker.main.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class RetrackerListReader {

    private final TaskRepository retrackerTaskRepository;
    private final RetrackerListRepository retrackerListRepository;
    private final RetrackerMapper retrackerMapper;
    private final UserTimeZoneService userTimeZoneService;
    private final UserService userService;



    public List<RetrackerListDto> getAllRetrackerLists() {
        var retrackerListDtos = new LinkedList<RetrackerListDto>();
        var retrackerLists = retrackerListRepository.findRetrackerListsForUser(userService.getUserIdOrCreateIfNew());
        var usersCurrentDate = LocalDate.now(userTimeZoneService.getUserTimeZone().toZoneId());
        for (var retrackerList : retrackerLists) {
            var dueCount = retrackerTaskRepository.countByRetrackerListIdAndDueDateLessThanEqual(retrackerList.getId(), usersCurrentDate);
            var dto = retrackerMapper.toRetrackerListDto(retrackerList, dueCount, false);
            retrackerListDtos.add(dto);
        }
        var invitations = retrackerListRepository.findRetrackerInvitationsForUser(userService.getUserIdOrCreateIfNew());
        for (var retrackerList : invitations) {
            var dueCount = 0L;
            var dto = retrackerMapper.toRetrackerListDto(retrackerList, dueCount, true);
            retrackerListDtos.add(dto);
        }
        return retrackerListDtos;
    }

}

