package org.mrsash.kalahapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mrsash.kalahapi.model.TurnType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMoveDto {

    @NotNull
    @NotBlank
    private String gameId;

    private int position;

    @NotNull
    private TurnType turn;
}
