package com.modsen.driverservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Create driver request")
public record CreateDriverRequest(
        @Schema(name = "First name", example = "John")
        @NotBlank(message = "Driver's first name must be defined")
        @Length(max = 25, message = "Passenger's first name must be shorter than 25 symbols")
        String firstName,

        @Schema(name = "Last name", example = "Doe")
        @NotBlank(message = "Driver's last name must be defined")
        @Length(max = 25, message = "Passenger's last name must be shorter than 25 symbols")
        String lastName,

        @Schema(name = "Licence number", example = "111222333")
        @NotBlank(message = "Driver's licence number must be defined")
        @Pattern(message = "Licence number is not valid", regexp = "^\\d{9}$")
        String licenceNumber,

        @Schema(name = "Email", example = "johndoe@example.com")
        @NotBlank(message = "Driver's email must be defined")
        @Email(message = "Email is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        String email,

        @Schema(name = "Phone number", example = "123-45-67")
        @NotBlank(message = "Driver's phone must be defined")
        @Pattern(message = "Phone number is not valid", regexp = "^\\d{3}-\\d{2}-\\d{2}$")
        String phone
) {
}
