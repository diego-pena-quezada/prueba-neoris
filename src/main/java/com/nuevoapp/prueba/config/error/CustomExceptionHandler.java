package com.nuevoapp.prueba.config.error;

import com.nuevoapp.prueba.config.error.dto.CustomError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Clase Advice que formatea las excepciones configuradas en formato JSON para estandarizar los mensajes de error.
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler{

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementExistsException (
            NoSuchElementException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.NO_CONTENT)
                .message(ex.getMessage())
                .code(HttpStatus.NO_CONTENT.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .code(HttpStatus.CONFLICT.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message("Credenciales inválidas")
                .code(HttpStatus.FORBIDDEN.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    @ExceptionHandler({MappingException.class})
    public ResponseEntity<Object> handleIMappingException(
            MappingException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Argumento Inválido")
                .errors(setErrorList(ex.getFieldErrors()))
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<Object> handleSecurityException(SecurityException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex){
        CustomError error = CustomError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message("Acceso denegado")
                .code(HttpStatus.FORBIDDEN.value())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        return buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(CustomError ex) {
        log.error("Error status={} message={} ", ex.getStatus(), ex.getMessage());
        return new ResponseEntity<>(ex, ex.getStatus());
    }

    private List<String> setErrorList(List<FieldError> errors){
        return errors.stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toList();
    }
}
