package com.boot.backend.Sweet.Shop.Management.System.exception;

public class SweetNotFoundException extends RuntimeException {

    public SweetNotFoundException(String message) {
        super(message);
    }

    public SweetNotFoundException(Long id) {
        super("Sweet not found with ID: " + id);
    }
}
