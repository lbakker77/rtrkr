package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.usecase.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteToListRequest extends BaseRequest {
    @NotNull
    private UUID listId;

    @NotNull
    @Email
    private String email;
}
