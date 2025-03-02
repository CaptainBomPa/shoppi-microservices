package me.fmroz.shoppi.exception;

public class ShippingInfoNotFoundException extends RuntimeException {
    public ShippingInfoNotFoundException(String message) {
        super(message);
    }
}
