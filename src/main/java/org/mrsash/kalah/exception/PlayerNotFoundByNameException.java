package org.mrsash.kalah.exception;

import org.springframework.http.HttpStatus;

public class PlayerNotFoundByNameException extends ABusinessException {

    public PlayerNotFoundByNameException(String name) {
        super(HttpStatus.NOT_FOUND, String.format("Player with name '%s' not found", name));
    }
}
