package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class RejectInvitationRequest extends BaseRequest {
    @NotNull
    private UUID listId;
}