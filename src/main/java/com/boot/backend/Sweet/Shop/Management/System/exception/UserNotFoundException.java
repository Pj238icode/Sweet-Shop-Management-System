package com.boot.backend.Sweet.Shop.Management.System.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
