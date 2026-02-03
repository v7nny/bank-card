package v7nny.bank.card.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.entity.enums.BankCardStatus;
import v7nny.bank.card.exception.bankcard.*;
import v7nny.bank.card.exception.user.UserNotFoundException;
import v7nny.bank.card.repository.BankCardRepository;
import v7nny.bank.card.util.CardNumberEncryptor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankCardUserServiceCreateTest {

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
        card.setStatus(BankCardStatus.ACTIVE);
        card.setExpiryDate(LocalDate.now().plusDays(1));
    }


    @Test
    void create_shouldSaveBankCardToDatabase_whenValidUserId() throws Exception {
        int userId = 1;
        var user = new BankCardUser();
        user.setId(userId);
        card.setUser(user);
        card.setMaskedCardNumber("maskedCardNumber");
        card.setEncryptedCardNumber("encryptedCardNumber");
        when(cardNumberEncryptor.maskCardNumber(anyString())).thenReturn(card.getMaskedCardNumber());
        when(cardNumberEncryptor.encryptCardNumber(anyString())).thenReturn(card.getEncryptedCardNumber());
        when(userService.findOneById(userId)).thenReturn(user);
        when(bankCardRepository.save(any(BankCard.class))).thenReturn(card);

        BankCard result = bankCardService.create(userId);

        assertNotNull(result);
        assertEquals("encryptedCardNumber", result.getEncryptedCardNumber());
        assertEquals("maskedCardNumber", result.getMaskedCardNumber());
        assertEquals(card.getExpiryDate(), result.getExpiryDate());
        assertEquals(user, result.getUser());
        verify(bankCardRepository, times(1)).save(any(BankCard.class));
        verify(userService, times(1)).findOneById(userId);
        verify(cardNumberEncryptor, times(1)).maskCardNumber(anyString());
        verify(cardNumberEncryptor, times(1)).encryptCardNumber(anyString());
    }

    @Test
    void create_shouldThrowUserNotFoundException_whenUserNotFound() throws CardNumberEncryptException, UserNotFoundException {
        int userId = 1;

        when(userService.findOneById(userId)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> bankCardService.create(userId));
        verify(userService).findOneById(userId);
        verify(cardNumberEncryptor, never()).maskCardNumber(anyString());
        verify(cardNumberEncryptor, never()).encryptCardNumber(anyString());
        verify(bankCardRepository, never()).save(any());
    }

}