package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.retracker.usecase.dtos.UserCategoryDto;
import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateRetrackerEntryRequest extends BaseRequest {
    private UUID listId;
    private String name;
    private ZonedDateTime dueDate;
    private ZonedDateTime lastEntryDate;
    private UserCategoryDto userCategory;
    private RecurrenceConfigDto recurrenceConfig;
}
