package ru.smartjava.backend.handlers;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import ru.smartjava.backend.entity.ResponseMessage;
import ru.smartjava.backend.exceptions.CustomBadRequestException;
import ru.smartjava.backend.exceptions.CustomInternalServerErrorException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
            CustomInternalServerErrorException.class
    })
    public ResponseEntity<?> handleCustomInternalServerError(Exception ex) {
        return ResponseEntity.internalServerError().body(new ResponseMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler({
            MultipartException.class,
            MethodArgumentNotValidException.class,
            FileSizeLimitExceededException.class,
            HttpMessageConversionException.class,
            MissingRequestValueException.class,
            CustomBadRequestException.class
    })
    public ResponseEntity<ResponseMessage> handleCustomBadRequestsException(
            Exception ex) {
        return ResponseEntity.badRequest().body(new ResponseMessage(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()));
    }

}
