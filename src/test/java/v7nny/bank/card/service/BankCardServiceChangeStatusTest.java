package v7nny.bank.card.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.enums.BankCardStatus;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.exception.bankcard.CardExpiredException;
import v7nny.bank.card.exception.bankcard.CardStatusAlreadySetException;
import v7nny.bank.card.repository.BankCardRepository;
import v7nny.bank.card.util.CardNumberEncryptor;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankCardServiceChangeStatusTest {

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
    void changeStatusById_shouldChangeActiveToBlocked() throws CardStatusAlreadySetException, BankCardNotFoundException, CardExpiredException {
        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        bankCardService.changeStatus(card.getId(), BankCardStatus.BLOCKED);

        verify(bankCardRepository).save(card);
        assertEquals(BankCardStatus.BLOCKED, card.getStatus());
    }

    @Test
    void changeStatusById_shouldChangeBlockedToActive() throws CardStatusAlreadySetException, BankCardNotFoundException, CardExpiredException {
        card.setStatus(BankCardStatus.BLOCKED);

        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        bankCardService.changeStatus(card.getId(), BankCardStatus.ACTIVE);

        verify(bankCardRepository).save(card);
        assertEquals(BankCardStatus.ACTIVE, card.getStatus());
    }

    @Test
    void changeStatusById_shouldAllowSettingExpiredStatusForExpiredCard() throws CardStatusAlreadySetException, BankCardNotFoundException, CardExpiredException {
        card.setExpiryDate(LocalDate.now().minusDays(2));

        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        bankCardService.changeStatus(card.getId(), BankCardStatus.EXPIRED);

        verify(bankCardRepository).save(card);
        assertEquals(BankCardStatus.EXPIRED, card.getStatus());
    }

    @Test
    void changeStatusById_shouldThrowBankCardNotFoundException_whenCardNotFound() {
        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.empty());

        assertThrows(BankCardNotFoundException.class, () ->
                bankCardService.changeStatus(card.getId(), BankCardStatus.BLOCKED
                ));
        verify(bankCardRepository, never()).save(any());
    }

    @Test
    void changeStatusById_shouldThrowCardStatusAlreadySetException_whenStatusSame() {
        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        assertThrows(CardStatusAlreadySetException.class, () ->
                bankCardService.changeStatus(card.getId(), BankCardStatus.ACTIVE
                ));
        verify(bankCardRepository, never()).save(any());
    }

    @Test
    void changeStatusById_shouldThrowCardExpiredException_whenCardExpired() {
        card.setExpiryDate(LocalDate.now().minusDays(2));

        when(bankCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        assertThrows(CardExpiredException.class, () ->
                bankCardService.changeStatus(card.getId(), BankCardStatus.BLOCKED
                ));
        assertEquals(BankCardStatus.EXPIRED, card.getStatus());
    }
}