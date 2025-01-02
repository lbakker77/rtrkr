package de.lbakker77.retracker.main.retracker.usecase.mapper;

import de.lbakker77.retracker.main.retracker.entity.model.RecurrenceConfig;
import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;
import de.lbakker77.retracker.main.retracker.entity.model.RetrackerList;
import de.lbakker77.retracker.main.retracker.entity.model.UserCategory;
import de.lbakker77.retracker.main.retracker.usecase.CreateRetrackerEntryRequest;
import de.lbakker77.retracker.main.retracker.usecase.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING  )
public interface RetrackerMapper {
    RetrackerListDto toRetrackerListDto(RetrackerList retrackerList, long dueCount);

    RetrackerEntryDto toRetrackerEntryDto(RetrackerEntry retrackerEntry);

    @Mapping( target = "recurrenceConfig", source = "recurrenceConfig" )
    @Mapping( target = "userCategory", source = "userCategory" )

    List<RetrackerOverviewEntryDto> toRetrackerOverviewEntryDtos(Iterable<RetrackerEntry> retrackerEntries);


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
