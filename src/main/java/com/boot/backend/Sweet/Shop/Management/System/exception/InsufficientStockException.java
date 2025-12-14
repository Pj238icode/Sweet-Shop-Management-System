package com.boot.backend.Sweet.Shop.Management.System.exception;


public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
