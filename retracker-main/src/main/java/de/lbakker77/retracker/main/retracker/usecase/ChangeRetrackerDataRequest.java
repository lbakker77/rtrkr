package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.retracker.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.retracker.usecase.dtos.UserCategoryDto;
import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChangeRetrackerDataRequest extends BaseRequest {
    private UUID id;
    private String name;
    private RecurrenceConfigDto recurrenceConfig;
    private UserCategoryDto userCategory;
}
