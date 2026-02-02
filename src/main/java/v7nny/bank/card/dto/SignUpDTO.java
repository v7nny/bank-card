package v7nny.bank.card.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record SignUpDTO(

        @NotBlank(message = "{validation.username.not-blank}")
        @Length(min = 5, max = 10, message = "{validation.username.length}")
        String username,

        @NotBlank(message = "{validation.email.not-blank}")
        @Email(message = "{validation.email}", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
        String email,

        @NotBlank(message = "{validation.password.not-blank}")
        @Length(min = 8, message = "{validation.password.length}")
        String password
) {}