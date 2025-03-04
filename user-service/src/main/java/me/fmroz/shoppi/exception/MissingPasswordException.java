package me.fmroz.shoppi.exception;

public class MissingPasswordException extends RuntimeException {
    public MissingPasswordException(String message) {
        super(message);
    }
}

