package org.mrsash.kalahapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMoveResultDto {

    private boolean finished;

    @NotNull
    private GameDto game;
}
