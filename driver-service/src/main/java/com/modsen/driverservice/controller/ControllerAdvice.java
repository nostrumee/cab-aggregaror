package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.response.AlreadyExistsResponse;
import com.modsen.driverservice.dto.response.ErrorResponse;
import com.modsen.driverservice.dto.response.ParamErrorResponse;
import com.modsen.driverservice.dto.response.ValidationErrorResponse;
import com.modsen.driverservice.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

import static com.modsen.driverservice.util.ErrorMessages.*;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DriverNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDriverNotFound(DriverNotFoundException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(DriverAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public AlreadyExistsResponse handleDriverAlreadyExists(DriverAlreadyExistsException e) {
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
