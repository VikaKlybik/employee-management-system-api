package com.klybik.management.exception.handler;

import com.klybik.management.exception.InvalidPasswordException;
import com.klybik.management.exception.JobTitleDeletedException;
import com.klybik.management.exception.SetLeadException;
import com.klybik.management.exception.UpdateLeadException;
import com.klybik.management.exception.dto.ErrorResponse;
import com.klybik.management.exception.dto.ErrorTypeEnum;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponse.builder()
                .errorType(ErrorTypeEnum.VALIDATION)
                .details(errors)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return ErrorResponse.builder()
                .errorType(ErrorTypeEnum.NOT_FOUND)
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidPasswordException.class, JobTitleDeletedException.class, SetLeadException.class, UpdateLeadException.class})
    public ErrorResponse handleLogicException(Exception ex) {
        return ErrorResponse.builder()
                .errorType(ErrorTypeEnum.LOGIC)
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateKeyException.class)
    public ErrorResponse handleDuplicateException(DuplicateKeyException ex) {
        return ErrorResponse.builder()
                .errorType(ErrorTypeEnum.DUPLICATE)
                .message(ex.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleOtherException(Exception ex) {
        return ErrorResponse.builder()
                .errorType(ErrorTypeEnum.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ErrorResponse handleAccessDeniedException() {
        return ErrorResponse.builder()
                .errorType(ErrorTypeEnum.AUTHENTICATION)
                .statusCode(HttpStatus.FORBIDDEN.value())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AuthenticationException.class})
    public ErrorResponse handleNotRightAuthException() {
        return ErrorResponse.builder()
                .errorType(ErrorTypeEnum.AUTHENTICATION)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }
}
