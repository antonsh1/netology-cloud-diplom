package ru.smartjava.backend.handlers;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.smartjava.backend.entity.ErrorMessage;
import ru.smartjava.backend.exceptions.CustomInternalServerError;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomInternalServerError.class)
    public ResponseEntity<?> handleCustomInternalServerError(CustomInternalServerError ex) {
        return ResponseEntity.internalServerError().body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleInternalErrorExceptions(
//            Exception ex) {
//        return ResponseEntity.ok(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера").toString());
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<String> handleBadRequestExceptions(
//            BadRequestException ex) {
//        return ResponseEntity.ok(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()).toString());
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
//    public ResponseEntity<ErrorMessage> handleHttpMediaTypeNotAcceptableException(
//            HttpMediaTypeNotAcceptableException ex) {
//        return
//                ResponseEntity.ok(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
//    }


    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorMessage> handleFileSizeLimitExceededException(
            FileSizeLimitExceededException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }



    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
}
