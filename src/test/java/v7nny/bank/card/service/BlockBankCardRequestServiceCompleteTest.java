package v7nny.bank.card.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.entity.BlockBankCardRequest;
import v7nny.bank.card.entity.enums.BankCardStatus;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.exception.bankcard.CardExpiredException;
import v7nny.bank.card.exception.bankcard.CardStatusAlreadySetException;
import v7nny.bank.card.exception.blockcardrequest.BlockBankCardRequestNotFoundException;
import v7nny.bank.card.repository.BlockBankCardRequestRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension .class)
public class BlockBankCardRequestServiceCompleteTest {

    @Mock
    private BlockBankCardRequestRepository blockBankCardRequestRepository;

    @Mock
    private BankCardService bankCardService;

    private BankCardUser user;

    private BankCard card;

    private BlockBankCardRequest blockRequest;

    @InjectMocks
    BlockBankCardRequestService blockBankCardRequestService;


    @BeforeEach
    void setUp() {
        user = new BankCardUser();
        user.setUsername("username");

        card = new BankCard();
        card.setId(1);
        card.setUser(user);

        blockRequest = new BlockBankCardRequest(card);
        blockRequest.setId(1);
    }

    @Test
    void completeBlockRequestById() throws BlockBankCardRequestNotFoundException, BankCardNotFoundException, CardStatusAlreadySetException, CardExpiredException {
        when(blockBankCardRequestRepository.findById(blockRequest.getId())).thenReturn(Optional.of(blockRequest));

        blockBankCardRequestService.completeBlockRequestById(blockRequest.getId());

        verify(blockBankCardRequestRepository, times(1)).findById(blockRequest.getId());
        verify(bankCardService, times(1))
                .changeStatus(card.getId(), BankCardStatus.BLOCKED);
        verify(blockBankCardRequestRepository, times(1)).save(any(BlockBankCardRequest.class));
    }

    @Test
    void completeBlockRequestById_ShouldThrowBlockBankCardRequestNotFoundException_WhenRequestNotFound() throws CardStatusAlreadySetException, BankCardNotFoundException, CardExpiredException {
        when(blockBankCardRequestRepository.findById(blockRequest.getId())).thenReturn(Optional.empty());

        assertThrows(BlockBankCardRequestNotFoundException.class, () ->
                blockBankCardRequestService.completeBlockRequestById(blockRequest.getId())
        );
        verify(bankCardService, never()).changeStatus(anyInt(), any());
        verify(blockBankCardRequestRepository, never()).save(any());
    }

    @Test
    void completeBlockRequestById_ShouldThrowCardStatusAlreadySetException() throws BankCardNotFoundException, CardStatusAlreadySetException, CardExpiredException {
        when(blockBankCardRequestRepository.findById(blockRequest.getId())).thenReturn(Optional.of(blockRequest));
        doThrow(new CardStatusAlreadySetException("Card already blocked"))
                .when(bankCardService).changeStatus(card.getId(), BankCardStatus.BLOCKED);

        assertThrows(CardStatusAlreadySetException.class,
                () -> blockBankCardRequestService.completeBlockRequestById(blockRequest.getId()));
        verify(blockBankCardRequestRepository, never()).save(any());
    }

    @Test
    void completeBlockRequestById_ShouldThrowCardExpiredException() throws BankCardNotFoundException, CardStatusAlreadySetException, CardExpiredException {
        when(blockBankCardRequestRepository.findById(blockRequest.getId())).thenReturn(Optional.of(blockRequest));
        doThrow(new CardExpiredException("Card expired"))
                .when(bankCardService).changeStatus(blockRequest.getBankCard().getId(), BankCardStatus.BLOCKED);

        assertThrows(CardExpiredException.class,
                () -> blockBankCardRequestService.completeBlockRequestById(blockRequest.getId()));
        verify(blockBankCardRequestRepository, never()).save(any());
    }
}