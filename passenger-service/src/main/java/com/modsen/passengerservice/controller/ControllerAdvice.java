package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.exception.ExceptionResponse;
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

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handlePassengerNotFound(PassengerNotFoundException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse("Validation failed");
        var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        exceptionResponse.setErrors(errors);
        return exceptionResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleConstraintViolation(ConstraintViolationException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse("Validation failed");
        var errors = new HashMap<String, String>();
        e.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });

        exceptionResponse.setErrors(errors);
        return exceptionResponse;
    }
}
