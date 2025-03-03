package de.lbakker77.retracker.main.usecase.list;

import de.lbakker77.retracker.core.usecase.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRetrackerListRequest extends BaseRequest {
    @NotNull
    @Size(min = 1, max = 20, message = "List name must be between 1 and 20 characters")
    private String name;

    @NotNull
    @Size(min = 1, max = 100, message = "Icon name must be between 1 and 100 characters")
    private String icon;
}