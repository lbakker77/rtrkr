package de.lbakker77.retracker.notification.usecase;

import de.lbakker77.retracker.core.usecase.BaseRequest;
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
public class MarkAsReadRequest extends BaseRequest {

    @NotNull
    private UUID notificationId;
}