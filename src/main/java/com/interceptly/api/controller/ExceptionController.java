package com.interceptly.api.controller;

import com.interceptly.api.model.ApiErrorModel;
import com.interceptly.api.util.ResponseEntityBuilder;
import com.interceptly.api.util.exceptions.GeneralException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> onConstraintValidationException(ConstraintViolationException ex){
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()){
            errors.add(violation.getPropertyPath().toString() + " " + violation.getMessage());
        }
        ApiErrorModel error = new ApiErrorModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                errors.toString()
        );
        return ResponseEntityBuilder.build(error);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        ApiErrorModel error = new ApiErrorModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                errors.toString()
        );
        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler({GeneralException.class})
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        log.error(ex.getMessage());
        ApiErrorModel error = new ApiErrorModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementException(Exception ex) {
        log.error(ex.getMessage());
        ApiErrorModel error = new ApiErrorModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        if(ex.getMessage().toLowerCase().contains("access is denied")) {
            return new ResponseEntity<>("Unauthorized Access", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
        ApiErrorModel error = new ApiErrorModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler({ResponseStatusException.class})public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex){
        log.error(ex.getMessage());
        ApiErrorModel error = new ApiErrorModel(
                LocalDateTime.now(),
                ex.getStatus(),
                ex.getReason()
        );
        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        log.error(ex.getMessage());
        ApiErrorModel error = new ApiErrorModel(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handle(Exception exception) {
        if (exception instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

