package com.modsen.driverservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Update driver request")
public record UpdateDriverRequest(
        @Schema(name = "First name", example = "John")
        @NotBlank(message = "{firstname.not.blank}")
        @Length(max = 25, message = "{firstname.length}")
        String firstName,

        @Schema(name = "Last name", example = "Doe")
        @NotBlank(message = "{lastname.not.blank}")
        @Length(max = 25, message = "{lastname.length}")
        String lastName,

        @Schema(name = "Licence number", example = "111222333")
        @NotBlank(message = "{licence-number.not.blank}")
        @Pattern(message = "{licence-number.valid}", regexp = "^\\d{9}$")
        String licenceNumber,

        @Schema(name = "Email", example = "johndoe@example.com")
        @NotBlank(message = "{email.not.blank}")
        @Email(message = "{email.valid}", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        String email,

        @Schema(name = "Phone number", example = "123-45-67")
        @NotBlank(message = "{phone.not.blank}")
        @Pattern(message = "{phone.valid}", regexp = "^\\d{3}-\\d{2}-\\d{2}$")
        String phone
) {
}
