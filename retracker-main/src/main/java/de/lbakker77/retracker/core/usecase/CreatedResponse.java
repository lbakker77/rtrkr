package de.lbakker77.retracker.core.usecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedResponse extends BaseResponse  {
    private UUID id;
}
