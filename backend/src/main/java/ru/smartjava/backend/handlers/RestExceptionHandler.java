package ru.smartjava.backend.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.smartjava.backend.entity.ErrorMessage;
import ru.smartjava.backend.exceptions.BadRequestException;
import ru.smartjava.backend.exceptions.StorageFileNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {
    //    extends ResponseEntityExceptionHandler
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalErrorExceptions(
            Exception ex) {
        return ResponseEntity.ok(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера").toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestExceptions(
            BadRequestException ex) {
        return ResponseEntity.ok(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()).toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorMessage handleHttpMediaTypeNotAcceptableException(
            MissingServletRequestParameterException ex) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}
