package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InviteToListRequest extends BaseRequest {
    @NotNull
    private UUID listId;

    @NotNull
    @Email
    private String email;
}
