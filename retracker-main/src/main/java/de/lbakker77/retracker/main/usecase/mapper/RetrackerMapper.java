package de.lbakker77.retracker.main.usecase.mapper;

import de.lbakker77.retracker.main.domain.*;
import de.lbakker77.retracker.main.usecase.dtos.*;
import de.lbakker77.retracker.user.UserService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TimeZone;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING  )
public abstract class RetrackerMapper {
    private UserService userService;
    private MessageSource messageSource;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Mapping(source = "nameOverride", target = "name" )
    public abstract RetrackerListDto toRetrackerListDto(RetrackerList retrackerList, long dueCount, boolean isInvitation, boolean isOwner, String nameOverride);

    @Mapping(target = "history", expression = "java(toEntryHistoryDtos(task.getHistory(), timeZone))")
    public abstract RetrackerTaskDto toRetrackerTaskDto(Task task, @Context TimeZone timeZone);


    @Mapping( target = "recurrenceConfig", source = "recurrenceConfig" )
    @Mapping( target = "userCategory", source = "userCategory" )
    public abstract List<RetrackerTaskOverviewDto> toRetrackerOverviewTaskDtos(Iterable<Task> tasks, @Context TimeZone timeZone);

    public ZonedDateTime fromLocalDate(LocalDate date, @Context TimeZone timeZone) {
        return date == null ? null : date.atStartOfDay(timeZone.toZoneId());
    }

    public abstract List<EntryHistoryDto> toEntryHistoryDtos(SortedSet<EntryHistory> history, @Context TimeZone timeZone);

    public abstract RecurrenceConfigDto toRecurrenceConfigDto(RecurrenceConfig recurrenceConfig);

    public  TaskCategoryDto toTaskCategoryDto(TaskCategory category){
        var categoryName = messageSource.getMessage("taskcategory_" + category.name().toLowerCase(), null, userService.getPreferredLocale());
        return new TaskCategoryDto(category, categoryName);
    }

    public abstract RecurrenceConfig toRecurrenceConfig(RecurrenceConfigDto recurrenceConfig);

}
