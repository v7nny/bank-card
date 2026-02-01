package v7nny.bank.card.dto;

import java.math.BigDecimal;

public record ResultTransferDTO(BigDecimal balanceBeforeTransferOnFromCard, BigDecimal balanceAfterTransferOnFromCard,
                                BigDecimal balanceBeforeTransferOnToCard, BigDecimal balanceAfterTransferOnToCard) {}
