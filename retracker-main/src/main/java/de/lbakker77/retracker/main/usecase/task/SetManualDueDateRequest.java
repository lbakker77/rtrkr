package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SetManualDueDateRequest extends BaseRequest {
    @NotNull
    private UUID taskId;

    @NotNull
    private ZonedDateTime manualDueDate;
}