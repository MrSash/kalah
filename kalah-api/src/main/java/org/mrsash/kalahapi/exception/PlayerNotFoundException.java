package org.mrsash.kalahapi.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;

public class PlayerNotFoundException extends ABusinessException {

    public PlayerNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, String.format("Player with id '%s' not found", id));
    }
}
