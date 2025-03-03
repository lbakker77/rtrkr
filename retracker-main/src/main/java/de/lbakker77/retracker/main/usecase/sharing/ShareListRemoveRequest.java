package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.usecase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ShareListRemoveRequest extends BaseRequest {
    public ShareListRemoveRequest(UUID listId, UUID userIdToRemove) {
        this.listId = listId;
        this.userIdToRemove = userIdToRemove;
    }

    @NotNull
    private UUID listId;

    @NotNull
    private UUID userIdToRemove;
}