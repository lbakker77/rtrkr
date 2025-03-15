package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseTaskChangeRequest extends BaseRequest {
    @NotNull
    private UUID id;
}
