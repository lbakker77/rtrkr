package de.lbakker77.retracker.main.usecase.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class PostponeTaskRequest extends BaseTaskChangeRequest {

    @NotNull
    private ZonedDateTime postponedDate;
}
