package com.modsen.passengerservice.controller;

import com.modsen.passengerservice.exception.ExceptionResponse;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handlePassengerNotFound(PassengerNotFoundException e) {
        return new ExceptionResponse(e.getMessage());
    }

}
