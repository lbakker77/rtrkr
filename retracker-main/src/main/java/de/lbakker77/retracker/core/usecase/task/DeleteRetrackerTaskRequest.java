package de.lbakker77.retracker.main.core.usecase.task;

import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeleteRetrackerTaskRequest extends BaseRequest {
    @NotNull
    private UUID id;
}
