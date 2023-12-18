package org.mrsash.kalah.exception;

import java.util.UUID;
import org.mrsash.kalah.model.TurnType;
import org.springframework.http.HttpStatus;

public class GameIncorrectPositionException extends ABusinessException {

    public GameIncorrectPositionException(UUID gameId, TurnType turn, int position) {
        super(
                HttpStatus.BAD_REQUEST,
                String.format("Incorrect position (game id = %s, turn = %s, position = %s)", gameId, turn, position)
        );
    }
}
