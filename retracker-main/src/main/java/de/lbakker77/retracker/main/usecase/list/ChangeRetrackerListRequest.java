package de.lbakker77.retracker.main.usecase.list;

import de.lbakker77.retracker.core.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class ChangeRetrackerListRequest extends BaseRequest {
    @NotNull(message = "List ID cannot be null")
    private UUID id;

    @NotNull
    @Size(min = 1, max = 20, message = "List name must be between 1 and 20 characters")
    private String name;

    @NotNull
    @NotBlank(message = "List icon cannot be blank")
    private String icon;
}