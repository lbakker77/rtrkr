package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UndoLastCompletionRequest extends BaseTaskChangeRequest {
    public UndoLastCompletionRequest(UUID id) {
        super(id);
    }
}
