package org.mrsash.kalah.exception;

import org.springframework.http.HttpStatus;

public class PlayerAlreadyExistsByNameException extends ABusinessException {

    public PlayerAlreadyExistsByNameException(String name) {
        super(HttpStatus.CONFLICT, String.format("Player with name '%s' already exists", name));
    }
}
