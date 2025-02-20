package de.lbakker77.retracker.main.usecase.list;

import de.lbakker77.retracker.core.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeleteRetrackerListRequest extends BaseRequest {
    public DeleteRetrackerListRequest(UUID id) {
        this.id = id;
    }
    @NotNull(message = "List ID cannot be null")
    private UUID id;
}