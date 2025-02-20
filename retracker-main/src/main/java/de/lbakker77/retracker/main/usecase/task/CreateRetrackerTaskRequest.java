package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.usecase.dtos.UserCategoryDto;
import de.lbakker77.retracker.core.usercase.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateRetrackerTaskRequest extends BaseRequest {
    @NotNull
    private UUID listId;
    @NotEmpty
    @jakarta.validation.constraints.Size(min = 1, max = 30)
    private String name;
    private ZonedDateTime dueDate;
    private ZonedDateTime lastEntryDate;
    @NotNull
    private UserCategoryDto userCategory;
    private RecurrenceConfigDto recurrenceConfig;
}
