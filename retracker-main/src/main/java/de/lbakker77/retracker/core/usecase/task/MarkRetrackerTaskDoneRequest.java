package de.lbakker77.retracker.main.core.usecase.task;

import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class MarkRetrackerTaskDoneRequest extends BaseRequest {
    @NotNull
    private UUID id;
    @NotNull
    private ZonedDateTime doneAt;
}

