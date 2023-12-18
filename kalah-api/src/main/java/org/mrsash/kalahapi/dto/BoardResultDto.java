package org.mrsash.kalahapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mrsash.kalahapi.model.TurnType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResultDto {

    private boolean finished;

    @NotNull
    private TurnType nextTurn;

    @NotNull
    private BoardDto board;
}
