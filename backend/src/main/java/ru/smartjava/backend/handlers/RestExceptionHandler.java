package ru.smartjava.backend.handlers;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import ru.smartjava.backend.entity.ErrorMessage;
import ru.smartjava.backend.exceptions.BadRequestException;
import ru.smartjava.backend.exceptions.CustomInternalServerError;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ RuntimeException.class, CustomInternalServerError.class})
    public ResponseEntity<?> handleCustomInternalServerError(Exception ex) {
        return ResponseEntity.internalServerError().body(new ErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler({BadRequestException.class, MultipartException.class, MethodArgumentNotValidException.class, FileSizeLimitExceededException.class, HttpMessageConversionException.class, MissingRequestValueException.class})
    public ResponseEntity<ErrorMessage> handleCustomBadRequestsException(
            Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()));
    }

}
