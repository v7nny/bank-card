package v7nny.bank.card.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.repository.BankCardRepository;
import v7nny.bank.card.util.CardNumberEncryptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankCardUserServiceDeleteTest {

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
    void deleteById_WhenCardExists_ShouldDeleteSuccessfully() {
        when(bankCardRepository.deleteById(card.getId())).thenReturn(1);

        assertDoesNotThrow(() -> bankCardService.deleteById(card.getId()));
        verify(bankCardRepository, times(1)).deleteById(card.getId());
    }

    @Test
    void deleteById_WhenCardDoesNotExist_ShouldThrowBankCardNotFoundException() {
        when(bankCardRepository.deleteById(card.getId())).thenReturn(0);

        assertThrows(BankCardNotFoundException.class, () ->
            bankCardService.deleteById(card.getId())
        );
        verify(bankCardRepository, times(1)).deleteById(card.getId());
    }
}
