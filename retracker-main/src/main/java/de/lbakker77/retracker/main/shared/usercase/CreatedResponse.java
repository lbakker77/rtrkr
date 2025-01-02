package de.lbakker77.retracker.main.shared.usercase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreatedResponse extends BaseResponse  {
    private UUID id;
}
