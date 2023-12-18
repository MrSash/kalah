package org.mrsash.kalah.exception;

import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ABusinessException extends RuntimeException {

    private final long timestamp;
    private final HttpStatus httpStatus;
    private final String message;

    protected ABusinessException(HttpStatus httpStatus, String message) {
        this.timestamp = Instant.now().toEpochMilli();
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
