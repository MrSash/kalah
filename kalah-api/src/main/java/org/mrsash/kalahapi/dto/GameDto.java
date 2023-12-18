package org.mrsash.kalahapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mrsash.kalahapi.model.TurnType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    @NotBlank
    private UUID id;

    private UUID ownerId;

    @NotBlank
    private TurnType turn;

    @NotEmpty
    private BoardDto board;
}
