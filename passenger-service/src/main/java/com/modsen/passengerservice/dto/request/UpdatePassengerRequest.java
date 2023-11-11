package com.modsen.passengerservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Update passenger request")
public record UpdatePassengerRequest(
        @Schema(name = "First name", example = "John")
        @NotBlank(message = "Passenger's first name must be defined")
        @Length(max = 25, message = "Passenger's first name must be shorter than 25 symbols")
        String firstName,

        @Schema(name = "Last name", example = "Doe")
        @NotBlank(message = "Passenger's last name must be defined")
        @Length(max = 25, message = "Passenger's last name must be shorter than 25 symbols")
        String lastName,

        @Schema(name = "Email", example = "johndoe@example.com")
        @NotBlank(message = "Passenger's email must be defined")
        @Email(message = "Email is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        String email,

        @Schema(name = "Phone number", example = "123-45-67")
        @NotBlank(message = "Passenger's phone must be defined")
        @Pattern(message = "Phone number is not valid", regexp = "^\\d{3}-\\d{2}-\\d{2}$")
        String phone
) {
}
