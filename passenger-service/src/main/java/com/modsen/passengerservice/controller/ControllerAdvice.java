package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.response.ErrorResponse;
import com.modsen.passengerservice.dto.response.ParamErrorResponse;
import com.modsen.passengerservice.dto.response.ValidationErrorResponse;
import com.modsen.passengerservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final String REQUEST_PARAM_MISSING_MESSAGE = "Request param missing";
    private static final String INVALID_PARAMETER_TYPE_MESSAGE = "Invalid parameter type";

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePassengerNotFound(PassengerNotFoundException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidPageParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidPageParameter(InvalidPageParameterException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidSortingParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidSortingParameter(InvalidSortingParameterException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailTaken(EmailTakenException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(PhoneTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlePhoneTaken(PhoneTakenException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(errors)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintViolation(ConstraintViolationException e) {
        var errors = new HashMap<String, String>();
        e.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });

        return ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(errors)
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ParamErrorResponse handleMissingRequestParameter(MissingServletRequestParameterException e) {
        var error = Map.of(e.getParameterName(), e.getMessage());

        return ParamErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(REQUEST_PARAM_MISSING_MESSAGE)
                .errors(error)
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ParamErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException  e) {
        var error = Map.of(e.getName(), e.getMessage());

        return ParamErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_PARAMETER_TYPE_MESSAGE)
                .errors(error)
                .build();
    }
}
