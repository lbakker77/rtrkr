package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.domain.TaskCategory;
import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTaskDataRequest extends BaseTaskChangeRequest {
    @NotEmpty
    private String name;
    private RecurrenceConfigDto recurrenceConfig;
    @NotNull
    private TaskCategory category;
}
