package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseRequest;
import de.lbakker77.retracker.main.domain.TaskCategory;
import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateTaskRequest extends BaseRequest {
    @NotNull
    private UUID listId;
    @NotEmpty
    @Size(min = 1, max = 30)
    private String name;
    private ZonedDateTime dueDate;
    private ZonedDateTime lastEntryDate;
    private TaskCategory category;
    private RecurrenceConfigDto recurrenceConfig;
}
