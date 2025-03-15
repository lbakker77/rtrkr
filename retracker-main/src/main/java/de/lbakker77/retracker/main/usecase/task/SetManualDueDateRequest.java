package de.lbakker77.retracker.main.usecase.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class SetManualDueDateRequest extends BaseTaskChangeRequest {
    public SetManualDueDateRequest(UUID id, ZonedDateTime manualDueDate) {
        super(id);
        this.manualDueDate = manualDueDate;
    }
    @NotNull
    private ZonedDateTime manualDueDate;
}