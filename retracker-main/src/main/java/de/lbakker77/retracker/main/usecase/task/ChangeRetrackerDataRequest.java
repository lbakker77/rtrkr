package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.usecase.dtos.UserCategoryDto;
import de.lbakker77.retracker.core.usercase.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChangeRetrackerDataRequest extends BaseTaskChangeRequest {
    @NotEmpty
    private String name;
    private RecurrenceConfigDto recurrenceConfig;
    @NotNull
    private UserCategoryDto userCategory;
}
