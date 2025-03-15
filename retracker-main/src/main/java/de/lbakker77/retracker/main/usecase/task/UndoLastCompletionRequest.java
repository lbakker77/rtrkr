package de.lbakker77.retracker.main.usecase.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class UndoLastCompletionRequest extends BaseTaskChangeRequest {
    public UndoLastCompletionRequest(UUID id) {
        super(id);
    }
}
