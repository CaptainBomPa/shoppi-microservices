package me.fmroz.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex) {
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return "Unauthorized: " + ex.getReason();
        }
        return "Error: " + ex.getReason();
    }
}
