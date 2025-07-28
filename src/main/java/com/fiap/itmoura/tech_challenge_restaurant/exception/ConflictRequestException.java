package com.fiap.itmoura.tech_challenge_restaurant.exception;

public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException(String message) {
        super(message);
    }

    public ConflictRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
