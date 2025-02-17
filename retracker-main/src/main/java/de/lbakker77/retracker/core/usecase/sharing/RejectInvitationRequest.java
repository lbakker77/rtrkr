package de.lbakker77.retracker.core.usecase.sharing;

import de.lbakker77.retracker.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RejectInvitationRequest extends BaseRequest {
    @NotNull
    private UUID listId;
}