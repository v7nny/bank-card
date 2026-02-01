package v7nny.bank.card.dto;

import java.math.BigDecimal;

public record TransferDTO(int fromCardId, int toCardId, BigDecimal amount) {}