package v7nny.bank.card.dto;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record TransferDTO(

        @Min(value = 1, message = "{validation.id-min}")
        int fromCardId,

        @Min(value = 1, message = "{validation.id-min}")
        int toCardId,

        @Min(value = 1, message = "{validation.amount-min}")
        BigDecimal amount
) {}