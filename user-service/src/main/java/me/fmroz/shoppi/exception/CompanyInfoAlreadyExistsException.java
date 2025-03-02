package me.fmroz.shoppi.exception;

public class CompanyInfoAlreadyExistsException extends RuntimeException {
    public CompanyInfoAlreadyExistsException(String message) {
        super(message);
    }
}
