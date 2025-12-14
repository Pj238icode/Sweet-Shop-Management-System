package com.boot.backend.Sweet.Shop.Management.System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Helper method for JSON error response
    private ResponseEntity<?> buildResponse(HttpStatus status, String error, String message) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }

    // User Already Exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "User Already Exists", ex.getMessage());
    }


    // Invalid Login Credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid Credentials", ex.getMessage());
    }


    // User Not Found (Refresh Token / Other)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage());
    }


    // Refresh Token Not Found
    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<?> handleRefreshTokenNotFound(RefreshTokenNotFoundException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token", ex.getMessage());
    }


    // Refresh Token Expired
    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<?> handleRefreshTokenExpired(RefreshTokenExpiredException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Refresh Token Expired", ex.getMessage());
    }


    // Catch-all fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    // Sweet Not Found
    @ExceptionHandler(SweetNotFoundException.class)
    public ResponseEntity<?> handleSweetNotFound(SweetNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Sweet Not Found",
                ex.getMessage()
        );
    }

    //Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                errors.toString()
        );
    }
    // Insufficient Stock
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStock(InsufficientStockException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Insufficient Stock",
                ex.getMessage()
        );
    }
    // Image Upload Exception
    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<?> handleImageUpload(ImageUploadException ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Image Upload Failed",
                ex.getMessage()
        );
    }



}
