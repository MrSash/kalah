package org.mrsash.kalah.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;

public class GameNotFoundException extends ABusinessException {

    public GameNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, String.format("Game with id '%s' not found", id));
    }
}
