package org.mrsash.kalah.exception;

import java.util.UUID;
import org.mrsash.kalah.model.TurnType;
import org.springframework.http.HttpStatus;

public class GameIncorrectTurnException extends ABusinessException {

    public GameIncorrectTurnException(UUID gameId, TurnType clientTurn, TurnType severTurn) {
        super(
                HttpStatus.BAD_REQUEST,
                String.format(
                        "Incorrect turn (game id = %s, client turn = %s, server turn, %s)",
                        gameId,
                        clientTurn,
                        severTurn
                )
        );
    }
}
