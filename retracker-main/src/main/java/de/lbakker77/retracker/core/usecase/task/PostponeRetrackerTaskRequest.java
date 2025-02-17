package de.lbakker77.retracker.core.usecase.task;

import de.lbakker77.retracker.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class PostponeRetrackerTaskRequest extends BaseRequest {
    @NotNull
    private UUID id;
    @NotNull
    private ZonedDateTime postponedDate;
}
