package de.lbakker77.retracker.main.core.usecase.sharing;

import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InviteToListRequest extends BaseRequest {
    @NotNull
    private UUID listId;

    @NotNull
    @Email
    private String email;
}
