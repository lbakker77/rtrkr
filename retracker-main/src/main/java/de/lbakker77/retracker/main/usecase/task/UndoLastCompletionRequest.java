package de.lbakker77.retracker.main.usecase.task;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UndoLastCompletionRequest extends BaseTaskChangeRequest {
    public UndoLastCompletionRequest(UUID id) {
        super(id);
    }
}
