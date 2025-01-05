package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.entity.RetrackerEntryRepository;
import de.lbakker77.retracker.main.retracker.entity.RetrackerListRepository;
import de.lbakker77.retracker.main.retracker.entity.model.*;
import de.lbakker77.retracker.main.retracker.usecase.dtos.RetrackerEntryDto;
import de.lbakker77.retracker.main.retracker.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.retracker.usecase.dtos.RetrackerOverviewEntryDto;
import de.lbakker77.retracker.main.retracker.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.main.shared.exception.NotFoundException;
import de.lbakker77.retracker.main.shared.interceptor.UserTimeZoneService;
import de.lbakker77.retracker.main.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RetrackerDataReader {

    private final RetrackerEntryRepository retrackerEntryRepository;
    private final RetrackerListRepository retrackerListRepository;
    private final RetrackerMapper retrackerMapper;
    private final UserTimeZoneService userTimeZoneService;
    private final UserService userService;

    public List<RetrackerOverviewEntryDto> getRetrackerEntries(UUID retrackerListId) {
        retrackerListRepository.findById(retrackerListId).orElseThrow(() -> new NotFoundException("Retracker list not found"));
        return retrackerMapper.toRetrackerOverviewEntryDtos(retrackerEntryRepository.findByRetrackerListId(retrackerListId), userTimeZoneService.getUserTimeZone());
    }

    public RetrackerEntryDto getRetrackerEntryById(UUID retrackerEntryId) {
        var entry =  retrackerEntryRepository.findById(retrackerEntryId).orElseThrow(() -> new NotFoundException("Retracker entry not found"));
        return retrackerMapper.toRetrackerEntryDto(entry, userTimeZoneService.getUserTimeZone());
    }

    public List<RetrackerListDto> getAllRetrackerLists() {
        var retrackerListDtos = new LinkedList<RetrackerListDto>();
        var retrackerLists = retrackerListRepository.findRetrackerListsForUser(userService.getUserId());
        var usersCurrentDate = LocalDate.now(userTimeZoneService.getUserTimeZone().toZoneId());
        for (var retrackerList : retrackerLists) {
            var dueCount = retrackerEntryRepository.countByRetrackerListIdAndDueDateLessThanEqual(retrackerList.getId(), usersCurrentDate);
            var dto = retrackerMapper.toRetrackerListDto(retrackerList, dueCount);
            retrackerListDtos.add(dto);
        }
        return retrackerListDtos;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        generateList("Personal");
    }

    public void generateList(String listid) {
        var retrackerList = RetrackerList.builder().name(listid).shared(false).ownerId(UUID.fromString( "00000000-0000-0000-0000-000000000001")).build();
        var userCategory = new UserCategory("Private", UserCategoryColor.BLUE  );
        var recurrenceConfig = new RecurrenceConfig(1, RecurranceTimeUnit.MONTH);
        var entryHistory = new EntryHistory(LocalDate.now().minusDays(21), LocalDate.now().minusDays(21),0);
        var entryHistoryList = List.of(entryHistory);
        var recurrenceConfig2 = new RecurrenceConfig(3, RecurranceTimeUnit.MONTH);
        var retrackerEntry = RetrackerEntry.builder().name("Task").retrackerList(retrackerList).dueDate(LocalDate.now().plusDays(7)).userCategory(userCategory).recurrenceConfig(recurrenceConfig).history(entryHistoryList).lastEntryDate(LocalDate.now().minusDays(10)).build();
        var retrackerEntry2 = RetrackerEntry.builder().name("Task 2").retrackerList(retrackerList).dueDate(LocalDate.now().minusDays(7)).userCategory(userCategory).recurrenceConfig(recurrenceConfig2).lastEntryDate(LocalDate.now().minusDays(10)).history(entryHistoryList).build();
        var retrackerEntry3 = RetrackerEntry.builder().name("Task 3").retrackerList(retrackerList).dueDate(LocalDate.now().minusDays(7)).userCategory(userCategory).build();

        retrackerListRepository.save(retrackerList);
        retrackerEntryRepository.saveAll(List.of(retrackerEntry, retrackerEntry2, retrackerEntry3));


        var shareConfig = new ShareConfig(UUID.fromString( "00000000-0000-0000-0000-000000000001"),ShareStatus.ACCEPTED, ZonedDateTime.now());
        var retrackerListShared = RetrackerList.builder().name("Family").shared(true).ownerId(UUID.fromString( "00000000-0000-0000-0000-000000000003")).shareConfigEntries(List.of(shareConfig)).build();
        retrackerListRepository.save(retrackerListShared);


        System.out.println("Generated Entry: " + retrackerEntry.getId());
    }
}
