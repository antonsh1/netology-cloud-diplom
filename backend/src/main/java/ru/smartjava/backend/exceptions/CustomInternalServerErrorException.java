package ru.smartjava.backend.exceptions;

public class CustomInternalServerErrorException extends RuntimeException {
    public CustomInternalServerErrorException(String message) {
        super(message);
    }

}
