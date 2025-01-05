package de.lbakker77.retracker.main.retracker.usecase.mapper;

import de.lbakker77.retracker.main.retracker.entity.model.RecurrenceConfig;
import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;
import de.lbakker77.retracker.main.retracker.entity.model.RetrackerList;
import de.lbakker77.retracker.main.retracker.entity.model.UserCategory;
import de.lbakker77.retracker.main.retracker.usecase.CreateRetrackerEntryRequest;
import de.lbakker77.retracker.main.retracker.usecase.dtos.*;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING  )
public interface RetrackerMapper {
    RetrackerListDto toRetrackerListDto(RetrackerList retrackerList, long dueCount);

    RetrackerEntryDto toRetrackerEntryDto(RetrackerEntry retrackerEntry, @Context TimeZone timeZone);

    @Mapping( target = "recurrenceConfig", source = "recurrenceConfig" )
    @Mapping( target = "userCategory", source = "userCategory" )

    List<RetrackerOverviewEntryDto> toRetrackerOverviewEntryDtos(Iterable<RetrackerEntry> retrackerEntries, @Context TimeZone timeZone);

    default ZonedDateTime fromLocalDate(LocalDate date, @Context TimeZone timeZone) {
        return date == null ? null : date.atStartOfDay(timeZone.toZoneId());
    }

    RecurrenceConfigDto toRecurrenceConfigDto(RecurrenceConfig recurrenceConfig);

    UserCategoryDto toUserCategoryDto(UserCategory userCategory);

    RecurrenceConfig toRecurrenceConfig(RecurrenceConfigDto recurrenceConfig);

    UserCategory toUserCategory(UserCategoryDto userCategory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "retrackerList", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "postponedDays", ignore = true)
    @Mapping(target = "history",  expression="java(new java.util.LinkedList<>())")

    RetrackerEntry toRetrackerEntry(CreateRetrackerEntryRequest createRetrackerEntryRequest);
}
