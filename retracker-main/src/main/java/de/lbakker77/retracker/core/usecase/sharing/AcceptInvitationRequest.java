package de.lbakker77.retracker.core.usecase.sharing;

import de.lbakker77.retracker.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AcceptInvitationRequest extends BaseRequest {
    public AcceptInvitationRequest(UUID listId) {
        this.listId = listId;
    }

    @NotNull
    private UUID listId;
}