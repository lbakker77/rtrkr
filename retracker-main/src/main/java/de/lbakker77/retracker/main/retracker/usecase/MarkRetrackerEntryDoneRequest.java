package de.lbakker77.retracker.main.retracker.usecase;

import de.lbakker77.retracker.main.shared.usercase.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class MarkRetrackerEntryDoneRequest extends BaseRequest {
    private UUID id;
    private ZonedDateTime doneAt;
}

