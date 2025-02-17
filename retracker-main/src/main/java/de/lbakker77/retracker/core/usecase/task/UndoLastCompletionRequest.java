package de.lbakker77.retracker.main.core.usecase.task;

import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UndoLastCompletionRequest extends BaseRequest {
     @NotNull
        private UUID id;
}
