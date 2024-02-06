package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.dto.response.AlreadyExistsResponse;
import com.modsen.passengerservice.dto.response.ErrorResponse;
import com.modsen.passengerservice.dto.response.ParamErrorResponse;
import com.modsen.passengerservice.dto.response.ValidationErrorResponse;
import com.modsen.passengerservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

import static com.modsen.passengerservice.util.ErrorMessages.*;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePassengerNotFound(PassengerNotFoundException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(PassengerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public AlreadyExistsResponse handleDriverAlreadyExists(PassengerAlreadyExistsException e) {
        return AlreadyExistsResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();
    }

    @ExceptionHandler(InvalidRequestParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidRequestParam(InvalidRequestParamException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
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

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ACCESS_DENIED_MESSAGE)
                .build();
    }
}
