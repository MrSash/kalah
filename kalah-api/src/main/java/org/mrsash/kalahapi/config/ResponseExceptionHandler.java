package org.mrsash.kalahapi.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Objects;
import org.mrsash.kalahapi.exception.ABusinessException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Exception handler for business exceptions.
     *
     * @param ex {@link ABusinessException} abstract exception class for all business exceptions
     * @return HTTP exception response
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleBusinessException(ABusinessException ex) {
        var errorBody = new ErrorResponse(
                ex.getTimestamp(),
                ex.getHttpStatus().value(),
                ex.getMessage(),
                ex.getClass().toString()
        );
        logger.warn("BUSINESS EXCEPTION: " + errorBody, ex);
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(errorBody);
    }

    /**
     * Exception handler for validation exceptions in controller layer
     * which throw {@link MethodArgumentNotValidException}.
     *
     * @param ex      {@link MethodArgumentNotValidException} validation exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return HTTP exception response
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return getValidationResponseEntity(
                ex,
                status.value(),
                ex.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse("")
        );
    }

    /**
     * Exception handler for validation exceptions in controller layer
     * which throw {@link HandlerMethodValidationException}.
     *
     * @param ex      {@link HandlerMethodValidationException} validation exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return HTTP exception response
     */
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return getValidationResponseEntity(
                ex,
                status.value(),
                ex.getAllErrors().stream()
                        .map(MessageSourceResolvable::getDefaultMessage)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse("")
        );
    }

    /**
     * Exception handler for validation exceptions in all layers except controller.
     *
     * @param ex {@link ConstraintViolationException} validation exception
     * @return HTTP exception response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return getValidationResponseEntity(
                ex,
                HttpStatus.BAD_REQUEST.value(),
                ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .findFirst()
                        .orElse("")
        );
    }

    private ResponseEntity<Object> getValidationResponseEntity(Exception ex, int statusCode, String message) {
        var errorBody = new ErrorResponse(
                Instant.now().toEpochMilli(),
                statusCode,
                message,
                ex.getClass().toString()
        );
        logger.warn("VALIDATION EXCEPTION: " + errorBody, ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorBody);
    }

    private record ErrorResponse(
            long timestamp,
            int status,
            String message,
            String exception
    ) {}
}
