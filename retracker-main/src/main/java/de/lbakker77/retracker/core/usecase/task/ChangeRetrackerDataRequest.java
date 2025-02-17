package de.lbakker77.retracker.main.core.usecase.task;

import de.lbakker77.retracker.main.core.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.core.usecase.dtos.UserCategoryDto;
import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChangeRetrackerDataRequest extends BaseRequest {
    @NotNull
    private UUID id;
    @NotEmpty
    private String name;
    private RecurrenceConfigDto recurrenceConfig;
    @NotNull
    private UserCategoryDto userCategory;
}
