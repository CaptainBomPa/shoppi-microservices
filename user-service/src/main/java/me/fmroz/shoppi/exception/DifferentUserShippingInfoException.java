package me.fmroz.shoppi.exception;

public class DifferentUserShippingInfoException extends RuntimeException {
    public DifferentUserShippingInfoException(String message) {
        super(message);
    }
}
