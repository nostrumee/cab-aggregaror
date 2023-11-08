package com.modsen.passengerservice.dto.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private Integer status;
    private String message;
}
