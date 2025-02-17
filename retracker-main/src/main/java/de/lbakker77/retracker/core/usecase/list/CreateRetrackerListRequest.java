package de.lbakker77.retracker.core.usecase.list;

import de.lbakker77.retracker.shared.usercase.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRetrackerListRequest extends BaseRequest {
    @NotBlank(message = "List name cannot be blank")
    @Size(min = 1, max = 20, message = "List name must be between 1 and 20 characters")
    private String name;

    @NotBlank(message = "List icon cannot be blank")
    private String icon;
}