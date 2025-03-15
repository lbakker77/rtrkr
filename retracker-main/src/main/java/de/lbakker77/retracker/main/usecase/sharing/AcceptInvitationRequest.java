package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AcceptInvitationRequest extends BaseRequest {
    public AcceptInvitationRequest(UUID listId) {
        this.listId = listId;
    }

    @NotNull
    private UUID listId;
}