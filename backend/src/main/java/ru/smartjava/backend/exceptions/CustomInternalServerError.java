package ru.smartjava.backend.exceptions;

public class CustomInternalServerError extends RuntimeException {

    public CustomInternalServerError(String message) {
        super(message);
    }

}
