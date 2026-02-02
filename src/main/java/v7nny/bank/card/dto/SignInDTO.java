package v7nny.bank.card.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInDTO(
        @NotBlank(message = "{validation.login.not-blank}")
        String login,

        @NotBlank(message = "{validation.password.not-blank")
        String password
) {}
