package v7nny.bank.card.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.entity.enums.BankCardStatus;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.exception.bankcard.CardAccessDeniedException;
import v7nny.bank.card.repository.BankCardRepository;
import v7nny.bank.card.util.CardNumberEncryptor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankCardUserServiceGetBalanceTest {

    @Mock
    private BankCardRepository bankCardRepository;

    @Mock
    private BankCardUserService userService;

    @Mock
    private CardNumberEncryptor cardNumberEncryptor;

    private BankCardService bankCardService;

    private BankCard card;


    @BeforeEach
    void setUp() {
        bankCardService = new BankCardService(
                bankCardRepository, userService, cardNumberEncryptor, 4);

        card = new BankCard();
        card.setId(1);
    }

    @Test
    void getBalanceByUsername_shouldReturnBalance_whenUserHasAccess() throws CardAccessDeniedException, BankCardNotFoundException {
        var username = "username";
        var expectedBalance = new BigDecimal("50.50");
        var user = new BankCardUser();

        user.setUsername(username);
        card.setUser(user);
        card.setBalance(expectedBalance);

        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        BigDecimal actualBalance = bankCardService.getBalanceByUsername(card.getId(), username);

        assertEquals(expectedBalance, actualBalance);
        verify(bankCardRepository).findById(card.getId());
    }

    @Test
    void getBalanceByUsername_shouldThrowBankCardNotFoundException_whenCardNotFound() {
        var username = "username";

        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.empty());

        assertThrows(BankCardNotFoundException.class, () -> {
            bankCardService.getBalanceByUsername(card.getId(), username);
        });
        verify(bankCardRepository).findById(card.getId());
    }

    @Test
    void getBalanceByUsername_shouldThrowCardAccessDeniedException_whenUserHasNoAccess() {
        var actualUsername = "username";
        var requestUsername = "username1";
        var expectedBalance = new BigDecimal("50.50");
        var user = new BankCardUser();

        user.setUsername(actualUsername);
        card.setUser(user);
        card.setBalance(expectedBalance);

        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        assertThrows(CardAccessDeniedException.class, () -> {
            bankCardService.getBalanceByUsername(card.getId(), requestUsername);
        });
    }
}
