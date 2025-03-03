package de.lbakker77.retracker.main.usecase.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class PostponeTaskRequest extends BaseTaskChangeRequest {

    @NotNull
    private ZonedDateTime postponedDate;
}
