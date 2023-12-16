package org.mrsash.kalahapi.exception;

import org.springframework.http.HttpStatus;

public class PlayerNotFoundException extends ABusinessException {

    public PlayerNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, String.format("Player with id '%s' not found", id));
    }
}
