package org.mrsash.kalah.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mrsash.kalah.model.TurnType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMoveDto {

    private int position;

    @NotNull
    private TurnType turn;
}
