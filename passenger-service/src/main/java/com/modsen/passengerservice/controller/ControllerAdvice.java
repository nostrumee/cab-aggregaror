package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.exception.ExceptionResponse;
import com.modsen.passengerservice.dto.exception.ValidationErrorResponse;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;

@RestControllerAdvice
public class ControllerAdvice {

    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handlePassengerNotFound(PassengerNotFoundException e) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ValidationErrorResponse validationErrorResponse =
                new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), VALIDATION_FAILED_MESSAGE);

        var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        validationErrorResponse.setErrors(errors);
        return validationErrorResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintViolation(ConstraintViolationException e) {
        ValidationErrorResponse validationErrorResponse =
                new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), VALIDATION_FAILED_MESSAGE);

        var errors = new HashMap<String, String>();
        e.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });

        validationErrorResponse.setErrors(errors);
        return validationErrorResponse;
    }
}
