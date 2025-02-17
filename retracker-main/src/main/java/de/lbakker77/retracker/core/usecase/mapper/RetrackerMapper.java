package de.lbakker77.retracker.core.usecase.mapper;

import de.lbakker77.retracker.core.domain.*;
import de.lbakker77.retracker.core.usecase.dtos.*;
import de.lbakker77.retracker.core.domain.*;
import de.lbakker77.retracker.core.usecase.dtos.*;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TimeZone;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING  )
public interface RetrackerMapper {
    RetrackerListDto toRetrackerListDto(RetrackerList retrackerList, long dueCount, boolean isInvitation);

    @Mapping(target = "history", expression = "java(toEntryHistoryDtos(task.getHistory(), timeZone))")
    RetrackerTaskDto toRetrackerTaskDto(Task task, @Context TimeZone timeZone);


    @Mapping( target = "recurrenceConfig", source = "recurrenceConfig" )
    @Mapping( target = "userCategory", source = "userCategory" )
    List<RetrackerTaskOverviewDto> toRetrackerOverviewTaskDtos(Iterable<Task> tasks, @Context TimeZone timeZone);

    default ZonedDateTime fromLocalDate(LocalDate date, @Context TimeZone timeZone) {
        return date == null ? null : date.atStartOfDay(timeZone.toZoneId());
    }

    List<EntryHistoryDto> toEntryHistoryDtos(SortedSet<EntryHistory> history, @Context TimeZone timeZone);

    RecurrenceConfigDto toRecurrenceConfigDto(RecurrenceConfig recurrenceConfig);

    UserCategoryDto toUserCategoryDto(UserCategory userCategory);

    RecurrenceConfig toRecurrenceConfig(RecurrenceConfigDto recurrenceConfig);

    UserCategory toUserCategory(UserCategoryDto userCategory);
}
