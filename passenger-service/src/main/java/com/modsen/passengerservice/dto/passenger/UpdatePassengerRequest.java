package com.modsen.passengerservice.dto.passenger;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record UpdatePassengerRequest(
        @NotBlank(message = "Passenger's first name must be defined")
        @Length(max = 25, message = "Passenger's first name must be shorter than 25 symbols")
        String firstName,

        @NotBlank(message = "Passenger's last name must be defined")
        @Length(max = 25, message = "Passenger's last name must be shorter than 25 symbols")
        String lastName,

        @NotBlank(message = "Passenger's email must be defined")
        @Email(message = "Email is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        String email,

        @NotBlank(message = "Passenger's phone must be defined")
        @Pattern(message = "Phone number is not valid", regexp = "^\\d{3}-\\d{2}-\\d{2}$")
        String phone
) {
}
